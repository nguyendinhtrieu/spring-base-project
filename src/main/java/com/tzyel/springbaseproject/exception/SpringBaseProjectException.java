package com.tzyel.springbaseproject.exception;

import com.tzyel.springbaseproject.utils.MessageUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpringBaseProjectException extends RuntimeException {
    private ErrorObject error;

    public SpringBaseProjectException(ErrorObject errorObject) {
        super(errorObject.getMessage());
        this.error = errorObject;
    }

    public static class Builder {
        private Class<? extends SpringBaseProjectException> exceptionClass;
        private String errorCode;
        private String[] params;

        public Builder(Class<? extends SpringBaseProjectException> exceptionClass) {
            this.exceptionClass = exceptionClass;
        }

        public static Builder badRequest(String errorCode, String... params) {
            return new Builder(SbpBadRequestException.class).errorCode(errorCode).params(params);
        }

        public static Builder notFound(String errorCode, String... params) {
            return new Builder(SbpNotFoundException.class).errorCode(errorCode).params(params);
        }

        public static Builder forbidden(String errorCode, String... params) {
            return new Builder(SbpForbiddenException.class).errorCode(errorCode).params(params);
        }

        public static Builder conflict(String errorCode, String... params) {
            return new Builder(SbpConflictException.class).errorCode(errorCode).params(params);
        }

        public static Builder internalServerError(String errorCode, String... params) {
            return new Builder(SbpInternalServerErrorException.class).errorCode(errorCode).params(params);
        }

        private Builder exceptionClass(Class<? extends SpringBaseProjectException> exceptionClass) {
            this.exceptionClass = exceptionClass;
            return this;
        }

        private Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        private Builder params(String... params) {
            this.params = params;
            return this;
        }

        public SpringBaseProjectException build() {
            ErrorObject errorObject = new ErrorObject();
            errorObject.setCode(errorCode);
            if (params == null || params.length == 0) {
                errorObject.setMessage(MessageUtil.getMessage(errorCode));
            } else {
                errorObject.setMessage(MessageUtil.getMessage(errorCode, params));
            }
            if (exceptionClass == SbpNotFoundException.class) {
                return new SbpNotFoundException(errorObject);
            }
            if (exceptionClass == SbpForbiddenException.class) {
                return new SbpForbiddenException(errorObject);
            }
            if (exceptionClass == SbpConflictException.class) {
                return new SbpConflictException(errorObject);
            }
            if (exceptionClass == SbpBadRequestException.class) {
                return new SbpBadRequestException(errorObject);
            }
            if (exceptionClass == SbpInternalServerErrorException.class) {
                return new SbpInternalServerErrorException(errorObject);
            }
            return new SpringBaseProjectException(errorObject);
        }
    }
}
