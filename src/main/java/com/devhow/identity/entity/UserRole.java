package com.devhow.identity.entity;

public enum UserRole {

    USER("USER"), VALIDATED("VALIDATED");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
