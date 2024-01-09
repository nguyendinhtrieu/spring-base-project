package com.tzyel.springbaseproject.dto.product;

import com.tzyel.springbaseproject.constant.MessageCode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductDto {
    @NotNull(message = "{" + MessageCode.E0010005 + "}")
    private String name;
    private String note;
}
