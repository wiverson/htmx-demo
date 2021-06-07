package com.devhow.identity.user;

import com.devhow.identity.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.mail.AuthenticationFailedException;
import java.util.Date;

import static com.devhow.identity.user.IdentityServiceException.Reason.BAD_PASSWORD_RESET;
import static com.devhow.identity.user.IdentityServiceException.Reason.BAD_TOKEN;

@Controller
@RequestMapping("/public")
public class UserController {

    static final String MESSAGE = "message";
    static final String ERROR = "error";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/ping", produces = "text/plain")
    @ResponseBody
    String ping(@RequestParam(name = "debug", required = false) String debug) {
        if (debug != null && debug.length() > 0) {
            return "OK " + new Date().toString() + " " + debug;
        }
        return "OK " + new Date().toString();
    }

    @PostMapping("/do-sign-in")
    public String doSignIn(@RequestParam(name = "error", defaultValue = "") String error, ModelMap modelMap) {
        if (error.length() > 0)
            modelMap.addAttribute(ERROR, error);

        return "index";
    }

    @GetMapping("/sign-in")
    String signIn(@RequestParam(name = "error", defaultValue = "") String error, @RequestParam(name = "message", defaultValue = "") String message, ModelMap modelMap) {
        if (message.length() > 0)
            modelMap.addAttribute(MESSAGE, message);
        if (error.length() > 0)
            modelMap.addAttribute(ERROR, "Invalid Login");
        return "identity/sign-in";
    }

    @GetMapping("/password-reset")
    String passwordResetKey(@RequestParam(name = "token") String key, ModelMap modelMap) {
        modelMap.put("key", key);
        return "identity/password-reset";
    }

    @PostMapping("/password-reset")
    String updatePassword(@RequestParam(name = "key") String key, @RequestParam(name = "email") String email,
                          @RequestParam(name = "password1") String password1, @RequestParam(name = "password2") String password2, ModelMap modelMap) {

        try {
            if (password1.compareTo(password2) != 0)
                throw new IdentityServiceException(BAD_PASSWORD_RESET, "Passwords don't match");
            userService.updatePassword(email, key, password1);
            return signIn("", "Password successfully updated.", modelMap);
        } catch (IdentityServiceException e) {
            modelMap.addAttribute(MESSAGE, e.getMessage());
        }
        return signIn("", "", modelMap);
    }

    @GetMapping("/forgot-password")
    String forgotPassword() {
        return "identity/forgot-password";
    }

    @PostMapping("/forgot-password")
    String resetPassword(@RequestParam(name = "email", defaultValue = "") String email, ModelMap modelMap) {

        try {
            userService.requestPasswordReset(email);
            return signIn("", "Check your email for password reset link.", modelMap);
        } catch (IdentityServiceException e) {
            if (e.getReason().equals(BAD_TOKEN))
                modelMap.addAttribute(MESSAGE, "Unknown Token");
            else
                modelMap.addAttribute(MESSAGE, e.getMessage());
        } catch (AuthenticationFailedException authenticationFailedException) {
            modelMap.addAttribute(MESSAGE, "Unable to send email right now...");
        }

        return "identity/forgot-password";
    }


    @GetMapping("/sign-up")
    String signUpPage(User user) {
        return "identity/sign-up";
    }

    @PostMapping("/sign-up")
    String signUp(User user, ModelMap modelMap) {
        try {
            userService.signUpUser(user.getUsername(), user.getPassword(), false);
            return "redirect:/public/sign-in?message=Check%20your%20email%20to%20confirm%20your%20account%21";
        } catch (IdentityServiceException e) {
            modelMap.addAttribute(ERROR, "Bad Sign Up");
        } catch (AuthenticationFailedException authenticationFailedException) {
            modelMap.addAttribute(ERROR, "Can't send email - email server is down/unreachable.");
            authenticationFailedException.printStackTrace();
        }
        return "identity/sign-up";
    }

    @GetMapping("/sign-up/confirm")
    String confirmMail(@RequestParam("token") String token, ModelMap modelMap) {
        try {
            userService.confirmUser(token).orElseThrow(() -> new IdentityServiceException(BAD_TOKEN));
            return signIn("", "Email Address Confirmed!", modelMap);
        } catch (IdentityServiceException e) {
            return signIn("", "Unknown Token", modelMap);
        }
    }
}
