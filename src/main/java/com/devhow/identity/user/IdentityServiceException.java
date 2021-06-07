package com.devhow.identity.user;

public class IdentityServiceException extends Exception {

    private final Reason reason;

    public IdentityServiceException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public IdentityServiceException(Reason reason) {
        super();
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }

    public enum Reason {
        BAD_EMAIL,
        BAD_LOGIN,
        BAD_PASSWORD,
        BAD_PASSWORD_RESET,
        BAD_TOKEN
    }
}
