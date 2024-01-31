package com.tzyel.springbaseproject.config;

import com.tzyel.springbaseproject.constant.MessageCode;
import com.tzyel.springbaseproject.constant.ViewHtmlConst;
import com.tzyel.springbaseproject.exception.ErrorObject;
import com.tzyel.springbaseproject.exception.SbpBadRequestException;
import com.tzyel.springbaseproject.exception.SbpConflictException;
import com.tzyel.springbaseproject.exception.SbpForbiddenException;
import com.tzyel.springbaseproject.exception.SbpNotFoundException;
import com.tzyel.springbaseproject.exception.SpringBaseProjectException;
import com.tzyel.springbaseproject.utils.ApplicationUtil;
import com.tzyel.springbaseproject.utils.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomizedExceptionHandler {
    @Value("${application.debug:false}")
    private boolean debug;

    @ExceptionHandler(NoResourceFoundException.class)
    public final Object handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        log.error(ex.getMessage());
        HttpStatus status = HttpStatus.NOT_FOUND;
        return handleErrorResponse(request, status, () -> {
            ErrorObject errorObject = handleExceptionAndGetErrorObject(ex, MessageCode.E0010002);
            return new ResponseEntity<>(errorObject, status);
        });
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleErrorResponse(request, status, ex, () -> {
            ErrorObject errorObject = handleExceptionAndGetErrorObject(ex, MessageCode.E0010005);
            Map<String, List<String>> errors = ex.getAllErrors().stream()
                    .collect(Collectors.groupingBy(
                            error -> ((FieldError) error).getField(),
                            Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));
            errorObject.setDetails(errors);
            return new ResponseEntity<>(errorObject, status);
        });
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final Object handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleErrorResponse(request, status, ex, () -> {
            ErrorObject errorObject = handleExceptionAndGetErrorObject(ex, MessageCode.E0010005);
            return new ResponseEntity<>(errorObject, status);
        });
    }

    @ExceptionHandler(SpringBaseProjectException.class)
    public final Object handleSpringBaseProjectException(SpringBaseProjectException ex, HttpServletRequest request) {
        HttpStatus status;
        if (ex instanceof SbpNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof SbpForbiddenException) {
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof SbpConflictException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof SbpBadRequestException) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return handleErrorResponse(request, status, ex, () -> {
            ErrorObject errorObject = ex.getError();
            if (errorObject == null) {
                errorObject = new ErrorObject();
                errorObject.setCode(MessageCode.E0010001);
                errorObject.setMessage(ex.getMessage());
            }

            setLogDebug(errorObject, ex.getStackTrace());

            return new ResponseEntity<>(errorObject, status);
        });
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final Object handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return handleErrorResponse(request, status, ex, () -> {
            ErrorObject errorObject = handleExceptionAndGetErrorObject(ex, MessageCode.E0010004);
            return new ResponseEntity<>(errorObject, status);
        });
    }

    @ExceptionHandler(Exception.class)
    public final Object handleException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleErrorResponse(request, status, ex, () -> {
            ErrorObject errorObject = handleExceptionAndGetErrorObject(ex, MessageCode.E0010001);
            return new ResponseEntity<>(errorObject, status);
        });
    }

    private Object handleErrorResponse(HttpServletRequest request, HttpStatus status, Supplier<Object> handleApiResponse) {
        if (ApplicationUtil.isWebRequest(request)) {
            return errorPageView(status);
        }
        return handleApiResponse.get();
    }

    private Object handleErrorResponse(HttpServletRequest request, HttpStatus status, Exception ex, Supplier<Object> handleApiResponse) {
        log.error(ex.getMessage(), ex);
        return handleErrorResponse(request, status, handleApiResponse);
    }

    private ErrorObject handleExceptionAndGetErrorObject(Exception ex, String messageCode) {
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

    private Object errorPageView(HttpStatus status) {
        HttpStatus httpStatus =
                status == HttpStatus.NOT_FOUND || status == HttpStatus.FORBIDDEN
                        ? status
                        : HttpStatus.INTERNAL_SERVER_ERROR;

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorCode", httpStatus.value());
        modelAndView.setViewName(ViewHtmlConst.ERROR);
        return modelAndView;
    }
}
