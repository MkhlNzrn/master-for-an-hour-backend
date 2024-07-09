package org.example.exceptions;

public class BidAlreadyExistsException extends RuntimeException {
    public BidAlreadyExistsException(Long userId, Long taskId) {
        super("Bid already exists, userId: " + userId + ", taskId: " + taskId);
    }
}
