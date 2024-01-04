package com.tzyel.springbaseproject.exception;

public class SbpBadRequestException extends SpringBaseProjectException {
    public SbpBadRequestException(ErrorObject errorObject) {
        super(errorObject);
    }
}
