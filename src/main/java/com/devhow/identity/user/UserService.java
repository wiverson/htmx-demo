package com.devhow.identity.user;

import com.devhow.identity.entity.User;
import com.devhow.identity.entity.UserValidation;
import jakarta.mail.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.devhow.identity.user.IdentityServiceException.Reason.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserValidationRepository userValidationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    @Value("${external.server.address}")
    private String serverAddress;

    @Value("${spring.mail.username}")
    private String emailSender;

    public UserService(UserRepository userRepository, UserValidationRepository userValidationRepository,
                       PasswordEncoder passwordEncoder, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.userValidationRepository = userValidationRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
    }

    public UserValidation validation(User user) {
        return userValidationRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalArgumentException("Need to save the user before using validation"));
    }

    private void sendConfirmationMail(User user) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("ChangeNode: Finish Setting Up Your Account");
        mailMessage.setFrom(emailSender);
        mailMessage.setText(
                "Thank you for registering!\n" +
                        "Please click on the below link to activate your account.\n\n" +
                        serverAddress + "/public/sign-up/confirm?token="
                        + validation(user).getToken());

        emailSenderService.sendEmail(mailMessage);
    }

    private void sendPasswordResetLink(User user) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("ChangeNode: Password Reset Link");
        mailMessage.setFrom(emailSender);
        mailMessage.setText(
                "Here's your password reset link. Only valid for apx two hours.\n" +
                        "Please click on the below link to reset your account password.\n\n" +
                        serverAddress + "/public/password-reset?token="
                        + validation(user).getPasswordResetToken());

        emailSenderService.sendEmail(mailMessage);
    }


    private void checkEmailAddress(String address) throws IdentityServiceException {
        // Really, really basic validation to ensure the email address has an @ symbol that's not at the start or end
        if (!address.contains("@"))
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address (1)");
        if (address.endsWith("@"))
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address (2)");
        if (address.startsWith("@"))
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address (3)");
        if (address.length() < 5)
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address (4)");
        if (!address.contains("."))
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address (5)");
    }

    private void checkPassword(String password) throws IdentityServiceException {
        if (password == null)
            throw new IdentityServiceException(BAD_PASSWORD, "No password set.");

        if (password.length() < 12)
            throw new IdentityServiceException(BAD_PASSWORD, "Password is too short.");

        if (password.length() > 200)
            throw new IdentityServiceException(BAD_PASSWORD, "Password is too long.");

        if (!password.trim().equals(password))
            throw new IdentityServiceException(BAD_PASSWORD, "No spaces in password.");

        if (password.contains(" "))
            throw new IdentityServiceException(BAD_PASSWORD, "No spaces in password.");

        String clean = password.replaceAll("[^\\n\\r\\t\\p{Print}]", "");
        if (!password.equals(clean))
            throw new IdentityServiceException(BAD_PASSWORD, "No non-printable characters in password.");
    }

    public User signIn(String username, String pass) throws IdentityServiceException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IdentityServiceException(BAD_LOGIN, "Unknown Email"));

        if (!passwordEncoder.matches(pass, user.getPassword()))
            throw new IdentityServiceException(BAD_LOGIN, "Invalid Login (1)");

        return user;
    }

    public User signUpUser(String username, String pass, boolean isTest) throws IdentityServiceException, AuthenticationFailedException {

        // First normalize user email
        checkEmailAddress(username);
        checkPassword(pass);

        // First verify user doesn't already exist
        Optional<User> foundUser = userRepository.findByUsername(username);

        if (foundUser.isPresent()) {
            throw new IdentityServiceException(BAD_EMAIL, "Email already exists.");
        }

        User newUser = new User();
        newUser.setUsername(username.trim().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(pass));
        newUser.setTest(isTest);

        if (!passwordEncoder.matches(pass, newUser.getPassword()))
            throw new IllegalArgumentException("The passwordEncoder just failed to match an encoded password!");

        newUser = userRepository.save(newUser);

        UserValidation userValidation = new UserValidation(newUser);
        userValidation.newToken();
        userValidationRepository.save(userValidation);

        sendConfirmationMail(newUser);

        return newUser;
    }


    public void deleteUser(User user) {
        if (!user.isTest())
            throw new IllegalArgumentException("Can only delete test users!");

        userValidationRepository.deleteById(user.getId());
        userRepository.delete(user);
    }

    private User existingUserSignup(User user) throws AuthenticationFailedException {
        if (user.getTokenValidation() != null)
            return user;
        if (validation(user).tokenIsCurrent()) {
            sendConfirmationMail(user);
        } else {
            validation(user).newToken();
            userValidationRepository.save(validation(user));
        }
        return user;
    }

    public Optional<User> confirmUser(String confirmationToken) throws IdentityServiceException {

        UserValidation userValidation = userValidationRepository.findByToken(confirmationToken).orElseThrow(() ->
                new IdentityServiceException(BAD_TOKEN, "Invalid Token (21)"));

        User user = userRepository.findById(userValidation.getUser()).orElseThrow(() ->
                new IdentityServiceException(BAD_TOKEN, "Invalid Token (22)"));

        if (!validation(user).tokenIsCurrent())
            throw new IdentityServiceException(BAD_TOKEN, "");

        user.markTokenAsValid();
        User savedUser = userRepository.save(user);

        return Optional.of(savedUser);
    }

    public Optional<User> findUser(String username) {
        return userRepository.findByUsername(username);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public UserValidation update(UserValidation userValidation) {
        return userValidationRepository.save(userValidation);
    }

    public UserValidation requestPasswordReset(String username) throws IdentityServiceException, AuthenticationFailedException {
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new IdentityServiceException(BAD_PASSWORD_RESET, "Missing email address. (a)"));

        if (!user.validated())
            throw new IdentityServiceException(BAD_TOKEN, "User never activated (should resend activation email)");

        UserValidation uv = userValidationRepository.findById(user.getId()).orElseThrow(()
                -> new IdentityServiceException(BAD_PASSWORD_RESET, "No validation token found. (b)"));

        if (uv.getPasswordResetIssue() != null)
            if (uv.passwordValidationIsCurrent()) {
                return uv;
            }

        uv.newPasswordResetToken();
        uv = userValidationRepository.save(uv);

        sendPasswordResetLink(user);

        return uv;
    }

    public User updatePassword(String username, String passwordResetToken, String newPassword) throws IdentityServiceException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new IdentityServiceException(BAD_PASSWORD_RESET, "No user found with this email. (c)"));
        UserValidation userValidation = userValidationRepository.findById(user.getId()).orElseThrow(()
                -> new IdentityServiceException(BAD_PASSWORD_RESET, "No user validation token[s] found. (d)"));

        if (!userValidation.getPasswordResetToken().equals(passwordResetToken))
            throw new IdentityServiceException(BAD_PASSWORD_RESET, "Invalid/expired token. (e)");
        if (!userValidation.passwordValidationIsCurrent())
            throw new IdentityServiceException(BAD_PASSWORD_RESET, "Token expired. (f)");

        user.setPassword(passwordEncoder.encode(newPassword));

        // Clear the now no longer useful tokens.
        userValidation.setPasswordResetIssue(null);
        userValidation.setPasswordResetToken(null);
        userValidationRepository.save(userValidation);

        return userRepository.save(user);
    }
}
