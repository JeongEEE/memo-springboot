package com.example.Memo.exception;

public class ObjectNonexistentException extends RuntimeException {
    public ObjectNonexistentException() {
    }

    public ObjectNonexistentException(String message) {
        super(message);
    }
}
