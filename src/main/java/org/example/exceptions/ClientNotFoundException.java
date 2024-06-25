package org.example.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super("Client with id " + id + " not found");
    }

    public ClientNotFoundException(String username) {
        super("Client with username " + username + " not found");
    }
}
