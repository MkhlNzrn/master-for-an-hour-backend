package org.example.exceptions;

public class MasterAccessRequestNotFoundException extends RuntimeException {
    public MasterAccessRequestNotFoundException(Long id) {
        super("Master access request not found: " + id);
    }
}
