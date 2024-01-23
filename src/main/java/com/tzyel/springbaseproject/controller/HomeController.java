package com.tzyel.springbaseproject.controller;

import com.tzyel.springbaseproject.constant.ViewHtmlConst;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.file.attribute.UserPrincipal;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homepage(@AuthenticationPrincipal UserPrincipal user) {
        return ViewHtmlConst.HOME;
    }

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal UserPrincipal user) {
        if (user != null) {
            return ViewHtmlConst.REDIRECT_SLASH;
        }
        return ViewHtmlConst.LOGIN;
    }
}
