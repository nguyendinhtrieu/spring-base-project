package com.tzyel.springbaseproject.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    @JsonProperty("access_token")
    private String accessToken;
}
