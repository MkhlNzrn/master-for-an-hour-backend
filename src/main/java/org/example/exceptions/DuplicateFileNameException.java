package org.example.exceptions;

public class DuplicateFileNameException extends RuntimeException {
    public DuplicateFileNameException(String name) {
        super("You have uploaded files with the same name" + name);
    }
}
