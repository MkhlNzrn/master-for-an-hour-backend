package org.example.exceptions;

public class DocumentsCountLimitException extends RuntimeException {
    public DocumentsCountLimitException(int count, int limit) {
        super("The documents count limit is full, actual value is: " + count + " Limit value is: " + limit);
    }
}
