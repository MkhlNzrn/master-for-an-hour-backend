package org.example.exceptions;

public class NoCategoriesFoundException extends RuntimeException {
    public NoCategoriesFoundException() {
        super("No Categories found");
    }
}
