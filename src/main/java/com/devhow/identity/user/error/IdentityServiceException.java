package com.devhow.identity.user.error;

public abstract class IdentityServiceException extends Exception {
    public IdentityServiceException(String message) {
        super(message);
    }

    public IdentityServiceException() {
        super();
    }
}
