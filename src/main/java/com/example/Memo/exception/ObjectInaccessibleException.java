package com.example.Memo.exception;

public class ObjectInaccessibleException extends RuntimeException {
    public ObjectInaccessibleException() {
    }

    public ObjectInaccessibleException(String message) {
        super(message);
    }
}
