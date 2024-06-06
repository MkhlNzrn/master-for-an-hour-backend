package org.example.exceptions;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String role) {
        super("Invalid role " + role);
    }
}
