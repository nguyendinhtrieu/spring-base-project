package com.tzyel.springbaseproject.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EnumValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ReportAsSingleViolation
public @interface EnumValidator {
    Class<? extends Enum<?>> enumClazz();

    String fieldName();

    String message() default "Value is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
