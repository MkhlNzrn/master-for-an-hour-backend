package org.example.wrappers;

import org.example.exceptions.DuplicateFileNameException;

import java.util.HashSet;

public class PathSet<E> extends HashSet<E> {
    @Override
    public boolean add(E e) {
        if (this.contains(e)) {
            throw new DuplicateFileNameException(e.toString());
        }
        return super.add(e);
    }
}