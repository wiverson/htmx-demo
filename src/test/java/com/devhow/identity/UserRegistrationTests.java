package com.devhow.identity;

import com.devhow.identity.entity.User;
import com.devhow.identity.entity.UserValidation;
import com.devhow.identity.user.IdentityServiceException;
import com.devhow.identity.user.UserService;
import jakarta.mail.AuthenticationFailedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {"mail.test=true"})
public class UserRegistrationTests {

    final private String BCRYPT_TOKEN = "{bcrypt}$2a$";
    private final Random random = new Random();
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    private User user = new User(true);
    private UserValidation userValidation;

    @Test
    public void CheckCurrentTokenCalc() {
        assertThat(user.validated()).isFalse();
        user.markTokenAsValid();
        assertThat(user.validated()).isTrue();
    }

    @Test
    public void HappyPathRegistration() throws IdentityServiceException, AuthenticationFailedException {
        String pass = "this-is-just-a-test";
        String username = "wiverson+" + random.nextInt() + "@gmail.com";

        assertThrows(IdentityServiceException.class, () -> userService.signIn(username, pass));

        User signedUpUser = userService.signUpUser(username, pass, true);

        assertThat(userService.validation(signedUpUser).getToken()).isNotNull();
        assertThat(userService.validation(signedUpUser).getToken()).isNotEmpty();
        assertThat(signedUpUser.validated()).isFalse();

        String encryptedPass = signedUpUser.getPassword();
        assertThat(encryptedPass).contains(BCRYPT_TOKEN);

        Optional<User> confirmUser = userService.confirmUser(userService.validation(signedUpUser).getToken());

        assertThat(confirmUser.isPresent()).isTrue();
        assertThat(confirmUser.get().validated()).isTrue();
        assertThat(confirmUser.get().getPassword()).isEqualTo(encryptedPass);

        User secondFormUser = new User(user.getUsername(), pass, true);
        assertThat(secondFormUser.getPassword()).doesNotContain(BCRYPT_TOKEN);

        User signIn = userService.signIn(username, pass);
        assertThat(signIn).isNotNull();
        assertThat(signIn.getPassword()).contains(BCRYPT_TOKEN);
    }

    /**
     * Existing confirmed user tries to sign up again
     */
    @Test
    public void ExistingUserTriesToSignUpAgain() throws IdentityServiceException, AuthenticationFailedException {
        String username = "wiverson+" + random.nextInt() + "@gmail.com";
        String password = "test-this-is-just-for-testing";

        user = userService.signUpUser(username, password, true);
        assertThat(user.validated()).isFalse();

        Optional<User> confirmUser = userService.confirmUser(userService.validation(user).getToken());
        assertThat(confirmUser.isPresent()).isTrue();
        assertThat(confirmUser.get().validated()).isTrue();

        User secondSignup = new User(user.getUsername(), user.getPassword(), true);

        // This is the key flow here - if a user tries to sign up again but is already confirmed,
        // the returned user will show up as isEnabled.
        assertThrows(IdentityServiceException.class, () ->
                userService.signUpUser(username, password, true));
    }

    /**
     * Invalid token path
     */
    @Test
    public void InvalidToken() throws IdentityServiceException, AuthenticationFailedException {
        String username = "wiverson+" + random.nextInt() + "@gmail.com";
        String pass = "test-is-just-for-a-test";

        user = userService.signUpUser(username, pass, true);
        assertThat(user.validated()).isFalse();

        assertThrows(IdentityServiceException.class, () -> userService.confirmUser("garbage token"));

        user = userService.findUser(user.getUsername()).orElseThrow();
        assertThat(user.validated()).isFalse();
    }

    /**
     * Invalid email address
     */
    @Test()
    public void InvalidEmailAddress() {
        assertThrows(IdentityServiceException.class, () ->
                user = userService.signUpUser("garbage email", "test-this-is-just-for-testing", true));
        assertThrows(IdentityServiceException.class, () ->
                user = userService.signUpUser("", "test-this-is-just-for-testing", true));
        assertThrows(IdentityServiceException.class, () ->
                user = userService.signUpUser("a", "test-this-is-just-for-testing", true));
        assertThrows(IdentityServiceException.class, () ->
                user = userService.signUpUser("a.c", "test-this-is-just-for-testing", true));
    }

    /**
     * Invalid password
     */
    @Test
    public void InvalidPassword() {
        assertThrows(IdentityServiceException.class, () ->
                        userService.signUpUser("wiverson+test@gmail.com", "", true),
                "Empty password");

        assertThrows(IdentityServiceException.class, () ->
                        userService.signUpUser("wiverson+test@gmail.com", "123", true),
                "Too short password");

        assertThrows(IdentityServiceException.class, () ->
                        userService.signUpUser("wiverson+test@gmail.com", "299 929 2929", true),
                "Password has spaces");
    }

    /**
     * Expired token
     */
    @Test
    public void ExpiredToken() throws IdentityServiceException, AuthenticationFailedException {
        user = userService.signUpUser("wiverson+" + random.nextInt() + "@gmail.com", "test-is-just-for-a-test", true);
        assertThat(user.validated()).isFalse();

        long timeInMillis = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillis);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 5);

        UserValidation userValidation = userService.validation(user);
        userValidation.setTokenIssue(new Timestamp(cal.getTimeInMillis()));

        userService.update(userValidation);

        assertThrows(IdentityServiceException.class, () -> userService.confirmUser(userService.validation(user).getToken()));

        assertThat(userService.findUser(user.getUsername()).orElseThrow().validated()).isFalse();
    }

    @Test
    public void ResetPassword() throws IdentityServiceException, AuthenticationFailedException {
        String startingPassword = "test-is-just-for-a-test";
        String username = "wiverson+" + random.nextInt() + "@gmail.com";

        // password reset is requested but email doesn't exist
        assertThrows(IdentityServiceException.class, () -> userValidation = userService.requestPasswordReset(user.getUsername()));

        user = userService.signUpUser(username, startingPassword, true);

        // password reset is requested but token has not been validated
        assertThrows(IdentityServiceException.class, () -> userService.requestPasswordReset(user.getUsername()));

        userService.signIn(user.getUsername(), startingPassword);

        // Confirm user with token
        userService.confirmUser(userService.validation(user).getToken());

        userValidation = userService.requestPasswordReset(user.getUsername());

        assertThat(userValidation.getPasswordResetIssue()).isNotNull();
        assertThat(userValidation.getPasswordResetToken()).isNotNull();

        // password reset is requested for valid account but password reset token expired

        userService.requestPasswordReset(user.getUsername());

        UserValidation userValidation = userService.validation(user);

        String newPassword = "this-is-a-fancy-new-password";

        assertThrows(IdentityServiceException.class, () -> userService.signIn(user.getUsername(), newPassword));
        user = userService.signIn(user.getUsername(), startingPassword);

        user = userService.updatePassword(user.getUsername(), userValidation.getPasswordResetToken(), newPassword);
        assertThat(user.getPassword()).contains(BCRYPT_TOKEN);

        assertThrows(IdentityServiceException.class, () -> userService.signIn(user.getUsername(), startingPassword));
        userService.signIn(user.getUsername(), newPassword);
    }
}
