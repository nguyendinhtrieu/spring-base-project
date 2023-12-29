package com.tzyel.springbaseproject.controller;

import com.tzyel.springbaseproject.config.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("health")
public class HealthController {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void healthCheck() {
        String token = new JwtService().generateToken("my-name");
        System.out.println(token);
        Claims claims = new JwtService().extractAllClaims(token);
        System.out.println(claims);
    }
}
