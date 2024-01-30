package com.tzyel.springbaseproject.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class ApplicationUtil {
    public static boolean isApiRequest(HttpServletRequest request) {
        return request.getServletPath().startsWith("/api/");
    }

    public static boolean isWebRequest(HttpServletRequest request) {
        return !isApiRequest(request);
    }

    public static boolean isApiRequest(WebRequest webRequest) {
        return ((ServletWebRequest) webRequest).getRequest().getServletPath().startsWith("/api/");
    }

    public static boolean isWebRequest(WebRequest webRequest) {
        return !isApiRequest(webRequest);
    }
}
