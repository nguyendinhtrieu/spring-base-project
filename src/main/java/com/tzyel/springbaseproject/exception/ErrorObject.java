package com.tzyel.springbaseproject.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ErrorObject {
    private String code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object details;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object debug;
}
