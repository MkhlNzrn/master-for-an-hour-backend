package org.example.exceptions;

public class BidNotFoundException extends RuntimeException {
    public BidNotFoundException(Long id) {
        super("Bid not found, Id: " + id);
    }
}
