package com.tzyel.springbaseproject.constant;

public interface ViewHtmlConst {
    String LOGIN = "login";
    String ERROR = "error";
    String HOME = "home";
    String REDIRECT_SLASH = "redirect:/";

    String[] ANT_MATCHERS_RESOURCES = {
            "/css/**",
            "/bootstrap/**",
            "/js/**",
            "/images/**",
            "/vendor/**",
            "/fonts/**",
            "/favicon.ico"
    };
}
