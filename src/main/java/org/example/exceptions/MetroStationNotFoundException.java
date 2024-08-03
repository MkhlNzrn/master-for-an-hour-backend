package org.example.exceptions;

public class MetroStationNotFoundException extends RuntimeException {
    public MetroStationNotFoundException(Long id) {
        super("Metro station not found by ID: " + id);
    }

    public MetroStationNotFoundException(String name) {
        super("Metro station not found by name: " + name);
    }
}
