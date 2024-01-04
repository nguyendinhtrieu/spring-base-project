package com.tzyel.springbaseproject.exception;

public class SbpForbiddenException extends SpringBaseProjectException {
    public SbpForbiddenException(ErrorObject errorObject) {
        super(errorObject);
    }
}
