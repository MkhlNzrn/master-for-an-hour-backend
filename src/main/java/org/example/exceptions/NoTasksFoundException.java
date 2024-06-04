package org.example.exceptions;

public class NoTasksFoundException extends RuntimeException {
    public NoTasksFoundException() {
        super("No tasks found");
    }
}
