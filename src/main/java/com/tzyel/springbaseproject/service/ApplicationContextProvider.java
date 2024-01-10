package com.tzyel.springbaseproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Utility class providing static access to the Spring ApplicationContext
 * and methods for retrieving beans and properties from the application context.
 * <p>
 * This class allows static access to the ApplicationContext, enabling retrieval
 * of beans and properties without needing to inject the ApplicationContext
 * into every class needing access to Spring-managed components or properties.
 */
@Service
public class ApplicationContextProvider {
    private static ApplicationContext context;

    /**
     * Setter method for injecting the ApplicationContext instance.
     *
     * @param context The Spring ApplicationContext instance to be set
     */
    @Autowired
    public ApplicationContextProvider(ApplicationContext context) {
        ApplicationContextProvider.context = context;
    }

    /**
     * Retrieve the current ApplicationContext instance.
     *
     * @return The current ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * Retrieve a Spring-managed bean by its class type from the static context.
     *
     * @param tClass The class type of the bean to retrieve
     * @param <T>    The generic type of the bean
     * @return The Spring-managed bean of the specified class type
     */
    public static <T> T getBeanFromStatic(Class<T> tClass) {
        return context.getBean(tClass);
    }

    /**
     * Retrieve a Spring-managed bean by its bean name and class type from the static context.
     *
     * @param beanName The name of the bean to retrieve
     * @param tClass   The class type of the bean to retrieve
     * @param <T>      The generic type of the bean
     * @return The Spring-managed bean of the specified class type by its name
     */
    public static <T> T getBeanFromStatic(String beanName, Class<T> tClass) {
        return context.getBean(beanName, tClass);
    }

    /**
     * Retrieve a property value from the application context's environment by its name.
     *
     * @param propertyName The name of the property to retrieve
     * @return The value of the property as a String
     */
    public static String getProperty(String propertyName) {
        return context.getEnvironment().getProperty(propertyName);
    }

    /**
     * Retrieve a property value from the application context's environment by its name and type.
     *
     * @param propertyName The name of the property to retrieve
     * @param tClass       The class type of the property
     * @param <T>          The generic type of the property
     * @return The value of the property parsed as the specified type
     */
    public static <T> T getProperty(String propertyName, Class<T> tClass) {
        return context.getEnvironment().getProperty(propertyName, tClass);
    }
}
