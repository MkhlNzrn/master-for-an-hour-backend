package org.example.handlers;

import org.example.exceptions.InvalidRoleException;
import org.example.exceptions.MasterNotFoundException;
import org.example.exceptions.NoMastersFoundException;
import org.example.exceptions.NoTasksFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MasterNotFoundException.class)
    public ResponseEntity<?> handleMasterNotFoundException(MasterNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoMastersFoundException.class)
    public ResponseEntity<?> handleMastersNotFoundException(NoMastersFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoTasksFoundException.class)
    public ResponseEntity<?> handleMasterNotFoundException(NoTasksFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<?> handleInvalidRoleException(NoTasksFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
