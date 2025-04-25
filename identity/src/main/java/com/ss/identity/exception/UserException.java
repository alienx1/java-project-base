package com.ss.identity.exception;

public class UserException extends RuntimeException {
    public UserException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(Throwable throwable) {
        super(throwable);
    }

}
