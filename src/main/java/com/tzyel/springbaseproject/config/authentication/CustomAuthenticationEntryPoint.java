package com.tzyel.springbaseproject.config.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzyel.springbaseproject.constant.MessageCode;
import com.tzyel.springbaseproject.exception.ErrorObject;
import com.tzyel.springbaseproject.utils.MessageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setCode(MessageCode.E0010003);
        errorObject.setMessage(MessageUtil.getMessage(MessageCode.E0010003));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, errorObject);
        responseStream.flush();
    }
}
