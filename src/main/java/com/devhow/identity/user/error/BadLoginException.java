package com.devhow.identity.user.error;

public class BadLoginException extends IdentityServiceException {
    public BadLoginException(String message) {
        super(message);
    }
}
