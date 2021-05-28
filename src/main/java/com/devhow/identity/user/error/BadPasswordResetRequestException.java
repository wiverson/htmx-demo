package com.devhow.identity.user.error;

public class BadPasswordResetRequestException extends IdentityServiceException {
    public BadPasswordResetRequestException(String message) {
        super(message);
    }
}
