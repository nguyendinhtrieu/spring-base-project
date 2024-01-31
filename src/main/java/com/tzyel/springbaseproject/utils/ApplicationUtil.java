package com.tzyel.springbaseproject.utils;

import jakarta.servlet.http.HttpServletRequest;

public class ApplicationUtil {
    public static boolean isApiRequest(HttpServletRequest request) {
        return request.getServletPath().startsWith("/api/");
    }

    public static boolean isWebRequest(HttpServletRequest request) {
        return !isApiRequest(request);
    }
}
