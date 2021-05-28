package com.devhow.identity.user.error;

public class BadPasswordException extends IdentityServiceException {
    public BadPasswordException(String message) {
        super(message);
    }
}
