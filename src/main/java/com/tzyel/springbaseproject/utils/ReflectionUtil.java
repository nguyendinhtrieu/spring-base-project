package com.tzyel.springbaseproject.utils;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ReflectionUtil {
    /**
     * Invokes a method by its name on a given object using Java Reflection.
     *
     * @param methodName The name of the method to be invoked
     * @param object     The object on which the method should be called
     * @param params     The parameters to be passed to the method
     * @param <T>        The generic return type of the method
     * @return The result of invoking the method
     * @throws RuntimeException If there's an issue invoking the method by reflection
     */
    public static <T> T callMethod(String methodName, Object object, Object... params) {
        try {
            //noinspection unchecked
            return (T) object.getClass().getMethod(methodName).invoke(object, params);
        } catch (Exception e) {
            String message = "Fail to call method by reflection: method name = " + methodName + ", object: " + object.toString() + "\n" + "params: " + Arrays.toString(params);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Retrieves the value of a field from a given object using Java Reflection.
     *
     * @param object    The object from which to retrieve the field value
     * @param fieldName The name of the field to retrieve
     * @param <T>       The generic type of the field
     * @return The value of the field
     * @throws RuntimeException If there's an issue accessing the field by reflection
     */
    public static <T> T getField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            if (!field.canAccess(object)) {
                field.setAccessible(true);
            }
            //noinspection unchecked
            return (T) field.get(object);
        } catch (Exception e) {
            String message = "Fail to call method by reflection: field name = " + fieldName + ", object: " + object.toString();
            throw new RuntimeException(message, e);
        }
    }
}
