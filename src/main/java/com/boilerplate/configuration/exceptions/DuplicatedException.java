package com.boilerplate.configuration.exceptions;

public class DuplicatedException extends Exception {
    public DuplicatedException() {}
    public DuplicatedException(String message) {
        super(message);
    }

}