package com.tzyel.springbaseproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextProvider {
    private static ApplicationContext context;

    @Autowired
    public ApplicationContextProvider(ApplicationContext context) {
        ApplicationContextProvider.context = context;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static <T> T getBeanFromStatic(Class<T> tClass) {
        return ApplicationContextProvider.getApplicationContext().getBean(tClass);
    }

    public static <T> T getBeanFromStatic(String beanName, Class<T> tClass) {
        return ApplicationContextProvider.getApplicationContext().getBean(beanName, tClass);
    }
}
