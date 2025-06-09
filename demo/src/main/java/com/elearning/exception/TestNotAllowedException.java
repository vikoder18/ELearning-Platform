package com.elearning.exception;

public class TestNotAllowedException extends RuntimeException {
    public TestNotAllowedException(String message) {
        super(message);
    }
}
