package com.devhow.identity.user.error;

public class BadTokenException extends IdentityServiceException {
    public BadTokenException(String message) {
        super(message);
    }

    public BadTokenException() {
        super();
    }
}
