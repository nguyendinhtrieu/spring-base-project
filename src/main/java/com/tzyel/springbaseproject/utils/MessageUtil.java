package com.tzyel.springbaseproject.utils;

import com.tzyel.springbaseproject.service.ApplicationContextProvider;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.Locale;

public class MessageUtil {
    private static final Locale DEFAULT_LOCALE = new Locale("jp");
    private static MessageSource messageSource;

    private static MessageSource getMessageSource() {
        if (messageSource == null) {
            messageSource = ApplicationContextProvider.getBeanFromStatic(MessageSource.class);
        }
        return messageSource;
    }

    public static String getMessage(String errorCode) {
        return getMessage(DEFAULT_LOCALE, errorCode);
    }

    public static String getMessage(Locale locale, String errorCode) {
        MessageSource messageSource = getMessageSource();
        return messageSource.getMessage(errorCode, null, locale);
    }

    public static String getMessage(String errorCode, String... params) {
        return getMessage(DEFAULT_LOCALE, errorCode, params);
    }

    public static String getMessage(Locale locale, String errorCode, String... params) {
        MessageSource messageSource = getMessageSource();
        return messageSource.getMessage(errorCode, params, locale);
    }

    public static String getMessageWithParamCodes(String errorCode, String... paramCodes) {
        return getMessageWithParamCodes(DEFAULT_LOCALE, errorCode, paramCodes);
    }

    public static String getMessageWithParamCodes(Locale locale, String errorCode, String... paramCodes) {
        String[] params = Arrays.stream(paramCodes)
                .map(paramCode -> MessageUtil.getMessage(locale, paramCode))
                .toArray(String[]::new);
        return getMessage(locale, errorCode, params);
    }
}
