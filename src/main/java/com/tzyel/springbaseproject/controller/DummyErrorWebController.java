package com.tzyel.springbaseproject.controller;

import com.tzyel.springbaseproject.constant.MessageCode;
import com.tzyel.springbaseproject.exception.SpringBaseProjectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("dummy-error")
public class DummyErrorWebController {

    @PreAuthorize("hasRole('ADMIN_UNKNOWN')")
    @GetMapping("permission")
    public String adminPermission() {
        return "Success!";
    }

    @GetMapping("400")
    public String badRequest() {
        log.info("Example for badRequest response");
        throw SpringBaseProjectException.Builder.badRequest(MessageCode.E0010005).build();
    }

    @GetMapping("403")
    public String forbidden() {
        log.info("Example for forbidden response");
        throw SpringBaseProjectException.Builder.forbidden(MessageCode.E0010007).build();
    }

    @GetMapping("404")
    public String notFound() {
        log.info("Example for notFound response");
        throw SpringBaseProjectException.Builder.notFound(MessageCode.E0010002).build();
    }

    @GetMapping("409")
    public String conflict() {
        log.info("Example for conflict response");
        throw SpringBaseProjectException.Builder.conflict(MessageCode.E0010006).build();
    }

    @GetMapping("500")
    public String internalServerError() {
        log.info("Example for internalServerError response");
        throw SpringBaseProjectException.Builder.internalServerError(MessageCode.E0010001).build();
    }
}
