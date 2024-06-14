package org.example.exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super("Invalid email " + email);
    }
}
