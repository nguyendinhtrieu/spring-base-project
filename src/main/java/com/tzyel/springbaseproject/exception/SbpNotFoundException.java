package com.tzyel.springbaseproject.exception;

public class SbpNotFoundException extends SpringBaseProjectException {
    public SbpNotFoundException(ErrorObject errorObject) {
        super(errorObject);
    }
}
