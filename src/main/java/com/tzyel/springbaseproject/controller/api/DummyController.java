package com.tzyel.springbaseproject.controller.api;

import com.tzyel.springbaseproject.dto.DummyReqDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/dummy")
public class DummyController extends ApiBaseController {
    @GetMapping
    public String member(Principal principal) {
        return "Member!" + principal;
    }

    @PostMapping
    public String dummyPost(@Validated @RequestBody DummyReqDto dummyReqDto) {
        return "Success: " + dummyReqDto;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin")
    public String admin(Principal principal) {
        return "Admin!" + principal;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("user")
    public String user(Principal principal) {
        return "User!" + principal;
    }
}
