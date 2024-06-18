package org.example.exceptions;

public class PhotoCountLimitException extends RuntimeException {
    public PhotoCountLimitException(int count, int limit) {
        super("The photos count limit is full, actual value is: " + count + " Limit value is: " + limit);
    }
}
