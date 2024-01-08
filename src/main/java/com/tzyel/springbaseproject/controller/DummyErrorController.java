package com.tzyel.springbaseproject.controller;

import com.tzyel.springbaseproject.constant.MessageCode;
import com.tzyel.springbaseproject.exception.SpringBaseProjectException;
import com.tzyel.springbaseproject.utils.MessageUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("dummy-error")
public class DummyErrorController {

    @PreAuthorize("hasRole('ADMIN_UNKNOWN')")
    @GetMapping("permission")
    public String adminPermission() {
        return "Success!";
    }

    @GetMapping("400")
    public String badRequest() {
        System.out.println(MessageUtil.getMessage(Locale.getDefault(), MessageCode.W0010001));
        System.out.println(MessageUtil.getMessage(Locale.getDefault(), MessageCode.I0010001));
        System.out.println(MessageUtil.getMessage(Locale.getDefault(), MessageCode.P0010001));
        System.out.println(MessageUtil.getMessage(Locale.getDefault(), MessageCode.E0010001));
        System.out.println(MessageUtil.getMessageWithParamCodes(Locale.getDefault(), MessageCode.E0011000, MessageCode.P0010001));
        System.out.println(MessageUtil.getMessageWithParamCodes(Locale.getDefault(), MessageCode.E0011000, MessageCode.P0010001, MessageCode.P0010001));
        throw SpringBaseProjectException.Builder.badRequest(MessageCode.E0010005).build();
    }

    @GetMapping("403")
    public String forbidden() {
        throw SpringBaseProjectException.Builder.forbidden(MessageCode.E0010007).build();
    }

    @GetMapping("404")
    public String notFound() {
        throw SpringBaseProjectException.Builder.notFound(MessageCode.E0010002).build();
    }

    @GetMapping("409")
    public String conflict() {
        throw SpringBaseProjectException.Builder.conflict(MessageCode.E0010006).build();
    }

    @GetMapping("500")
    public String internalServerError() {
        throw SpringBaseProjectException.Builder.internalServerError(MessageCode.E0010001).build();
    }
}
