package com.tzyel.springbaseproject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("dummy")
public class DummyController extends BaseController {
    @GetMapping
    public String member(Principal principal) {
        return "Member!" + principal;
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
