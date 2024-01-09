package com.tzyel.springbaseproject.config;

import com.tzyel.springbaseproject.constant.MessageCode;
import com.tzyel.springbaseproject.exception.ErrorObject;
import com.tzyel.springbaseproject.exception.SbpBadRequestException;
import com.tzyel.springbaseproject.exception.SbpConflictException;
import com.tzyel.springbaseproject.exception.SbpForbiddenException;
import com.tzyel.springbaseproject.exception.SbpNotFoundException;
import com.tzyel.springbaseproject.exception.SpringBaseProjectException;
import com.tzyel.springbaseproject.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {
    @Value("${application.debug:false}")
    private boolean debug;

    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorObject errorObject = handleExceptionAndGetErrorObject(ex, MessageCode.E0010002);
        return this.handleExceptionInternal(ex, errorObject, headers, status, request);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorObject errorObject = handleExceptionAndGetErrorObject(ex, MessageCode.E0010005);
        Map<String, List<String>> errors = ex.getAllErrors().stream()
                .collect(Collectors.groupingBy(
                        error -> ((FieldError) error).getField(),
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));
        errorObject.setDetails(errors);
        return this.handleExceptionInternal(ex, errorObject, headers, status, request);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorObject errorObject = handleExceptionAndGetErrorObject(ex, MessageCode.E0010005);
        return this.handleExceptionInternal(ex, errorObject, headers, status, request);
    }

    @ExceptionHandler(SpringBaseProjectException.class)
    public final ResponseEntity<Object> handleSpringBaseProjectException(SpringBaseProjectException ex) {
        log.error(ex.getMessage(), ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorObject errorObject = ex.getError();
        setLogDebug(errorObject, ex.getStackTrace());

        if (ex instanceof SbpNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }
        if (ex instanceof SbpForbiddenException) {
            status = HttpStatus.FORBIDDEN;
        }
        if (ex instanceof SbpConflictException) {
            status = HttpStatus.CONFLICT;
        }
        if (ex instanceof SbpBadRequestException) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(errorObject, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorObject errorObject = handleExceptionAndGetErrorObject(ex, MessageCode.E0010004);
        return new ResponseEntity<>(errorObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception ex) {
        ErrorObject errorObject = handleExceptionAndGetErrorObject(ex, MessageCode.E0010001);
        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorObject handleExceptionAndGetErrorObject(Exception ex, String messageCode) {
        log.error(ex.getMessage(), ex);
        ErrorObject errorObject = new ErrorObject();
        errorObject.setCode(messageCode);
        errorObject.setMessage(MessageUtil.getMessage(messageCode));
        setLogDebug(errorObject, ex.getStackTrace());

        return errorObject;
    }

    private void setLogDebug(ErrorObject errorObject, StackTraceElement[] stackTraceElements) {
        if (debug) {
            errorObject.setDebug(stackTraceElements);
        }
    }
}
