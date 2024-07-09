package org.example.exceptions;

public class EmailPinNotFoundException extends RuntimeException {
    public EmailPinNotFoundException(String email) {
        super("Email validation request not found: " + email);
    }
}
