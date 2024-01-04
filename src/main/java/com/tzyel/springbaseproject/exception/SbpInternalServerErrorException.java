package com.tzyel.springbaseproject.exception;

public class SbpInternalServerErrorException extends SpringBaseProjectException {
    public SbpInternalServerErrorException(ErrorObject errorObject) {
        super(errorObject);
    }
}
