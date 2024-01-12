package com.tzyel.springbaseproject.util;

import com.tzyel.springbaseproject.utils.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;

@Slf4j
public class VerificationUtil {
    /**
     * Verifies if the fields of two objects are equal. Fields present in firstObject but not in secondObject are ignored.
     *
     * @param firstObject  The first object for comparison.
     * @param secondObject The second object for comparison.
     */
    public static void verifyFieldsEqual(Object firstObject, Object secondObject) {
        if (firstObject == null || secondObject == null) {
            Assertions.fail("Objects must not be null.");
        }

        Class<?> clazz = firstObject.getClass();
        Field[] firstObjectFields = clazz.getDeclaredFields();

        boolean atLeastOneMatch = false;

        for (Field firstObjectField : firstObjectFields) {
            firstObjectField.setAccessible(true);
            try {
                Object firstObjectFieldValue = firstObjectField.get(firstObject);
                Object secondObjectFieldValue = ReflectionUtil.getField(secondObject, firstObjectField.getName());

                if ((firstObjectFieldValue == null && secondObjectFieldValue != null)
                        || (firstObjectFieldValue != null && !firstObjectFieldValue.equals(secondObjectFieldValue))) {
                    Assertions.fail("Field '" + firstObjectField.getName() + "' values are not equal.");
                } else {
                    log.debug("Field '{}' has matching values.", firstObjectField.getName());
                    atLeastOneMatch = true;
                }
            } catch (Exception e) {
                log.debug("Field {} is not exist in the secondObject. Just ignore it.", firstObjectField.getName());
            }
        }

        if (!atLeastOneMatch) {
            Assertions.fail("No matching fields found between firstObject and secondObject.");
        }
    }
}
