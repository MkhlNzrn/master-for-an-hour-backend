package org.example.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Category not found by ID: " + id);
    }

    public CategoryNotFoundException(String name) {
        super("Category not found by name: " + name);
    }
}
