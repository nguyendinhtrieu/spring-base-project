package com.tzyel.springbaseproject.utils;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ReflectionUtil {
    public static <T> T callMethod(String methodName, Object object, Object... params) {
        try {
            //noinspection unchecked
            return (T) object.getClass().getMethod(methodName).invoke(object, params);
        } catch (Exception e) {
            String message = "Fail to call method by reflection: method name = " + methodName + ", object: " + object.toString() + "\n" + "params: " + Arrays.toString(params);
            throw new RuntimeException(message, e);
        }
    }

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
