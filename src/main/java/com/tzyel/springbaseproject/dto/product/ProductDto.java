package com.tzyel.springbaseproject.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProductDto {
    private Integer id;

    private String name;

    @JsonProperty("company_id")
    private Integer companyId;

    private String note;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonProperty("updated_at")
    private Timestamp updatedAt;
}
