package com.tzyel.springbaseproject.exception;

public class SbpNotFoundException extends SpringBaseProjectException {
    public SbpNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SbpNotFoundException(ErrorObject errorObject) {
        super(errorObject);
    }
}
