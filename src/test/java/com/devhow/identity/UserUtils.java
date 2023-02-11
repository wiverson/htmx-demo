package com.devhow.identity;

import com.devhow.identity.entity.User;
import com.devhow.identity.user.IdentityServiceException;
import com.devhow.identity.user.UserService;
import jakarta.mail.AuthenticationFailedException;

import java.util.Optional;
import java.util.Random;

public class UserUtils {

    private final static Random random = new Random();

    static public User setupUser(UserService userService) throws IdentityServiceException, AuthenticationFailedException {
        String pass = "this-is-just-a-test";
        return setupUser(userService, pass);
    }

    static public User setupUser(UserService userService, String pass) throws IdentityServiceException, AuthenticationFailedException {
        User user = userService.signUpUser("wiverson+" + random.nextInt() + "@gmail.com", pass, true);
        Optional<User> confirmUser = userService.confirmUser(userService.validation(user).getToken());

        return userService.signIn(confirmUser.orElseThrow().getUsername(), pass);
    }
}
