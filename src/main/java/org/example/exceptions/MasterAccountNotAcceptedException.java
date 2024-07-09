package org.example.exceptions;

public class MasterAccountNotAcceptedException extends RuntimeException {
    public MasterAccountNotAcceptedException(Long id) {
        super("Master account is not accepted, Id: " + id);
    }
}
