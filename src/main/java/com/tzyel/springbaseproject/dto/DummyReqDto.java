package com.tzyel.springbaseproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tzyel.springbaseproject.constant.MessageCode;
import com.tzyel.springbaseproject.constant.enums.DummyEnum;
import com.tzyel.springbaseproject.validation.DateTimeValidator;
import com.tzyel.springbaseproject.validation.EnumValidator;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DummyReqDto {
    @JsonProperty("first_field")
    @NotNull(message = "{" + MessageCode.E0010005 + "}")
    public String firstField;

    @JsonProperty("second_field")
    @EnumValidator(enumClazz = DummyEnum.class, fieldName = "valueAsString")
    public String secondField;

    @JsonProperty("third_field")
    @EnumValidator(enumClazz = DummyEnum.class, fieldName = "valueAsNumber", message = "{" + MessageCode.E0010008 + "}")
    public Integer thirdField;

    @JsonProperty("local_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    public LocalDate localDate;

    @JsonProperty("local_date_string")
    @DateTimeValidator(pattern = "yyyyMMdd")
    public String localDateString;
}
