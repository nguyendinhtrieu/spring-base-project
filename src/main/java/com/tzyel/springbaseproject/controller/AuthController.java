package com.tzyel.springbaseproject.controller;

import com.tzyel.springbaseproject.constant.MessageCode;
import com.tzyel.springbaseproject.dto.auth.LoginRequestDto;
import com.tzyel.springbaseproject.dto.auth.LoginResponseDto;
import com.tzyel.springbaseproject.service.JwtService;
import com.tzyel.springbaseproject.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("login")
    public LoginResponseDto login(@Validated @RequestBody LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return new LoginResponseDto(jwtService.generateToken(loginRequestDto.getUsername()));
        } else {
            throw new UsernameNotFoundException(MessageUtil.getMessage(MessageCode.E0010002));
        }
    }
}
