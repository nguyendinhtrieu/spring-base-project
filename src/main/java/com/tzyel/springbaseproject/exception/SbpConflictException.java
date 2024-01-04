package com.tzyel.springbaseproject.exception;

public class SbpConflictException extends SpringBaseProjectException {
    public SbpConflictException(ErrorObject errorObject) {
        super(errorObject);
    }
}
