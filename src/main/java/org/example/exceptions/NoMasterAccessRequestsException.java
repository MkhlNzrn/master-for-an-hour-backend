package org.example.exceptions;

public class NoMasterAccessRequestsException extends RuntimeException {
    public NoMasterAccessRequestsException() {
        super("No Master Access Requests");
    }
}
