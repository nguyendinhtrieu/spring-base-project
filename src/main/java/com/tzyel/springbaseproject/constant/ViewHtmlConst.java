package com.tzyel.springbaseproject.constant;

public interface ViewHtmlConst {
    String LOGIN = "login";
    String ERROR = "error";
    String DASHBOARD = "dashboard";
    String REDIRECT_SLASH = "redirect:/";

    String[] ANT_MATCHERS_RESOURCES = {
            "/css/**",
            "/bootstrap/**",
            "/static/js/**",
            "/images/**",
            "/vendor/**",
            "/fonts/**",
            "/favicon.ico"
    };
}
