package com.devhow.identity.user.error;

public class BadEmailException extends IdentityServiceException {
    public BadEmailException(String message) {
        super(message);
    }
}
