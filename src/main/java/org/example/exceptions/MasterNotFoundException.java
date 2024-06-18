package org.example.exceptions;

public class MasterNotFoundException extends RuntimeException {
    public MasterNotFoundException(Long id) {
        super("Master with id " + id + " not found");
    }

    public MasterNotFoundException(String username) {
        super("Master with username " + username + " not found");
    }
}
