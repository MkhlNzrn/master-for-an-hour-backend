package org.example.exceptions;

public class NoMastersFoundException extends RuntimeException {
    public NoMastersFoundException() {
        super("No masters found");
    }
}
