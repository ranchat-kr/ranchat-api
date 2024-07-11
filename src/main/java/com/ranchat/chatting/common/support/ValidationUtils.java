package com.ranchat.chatting.common.support;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

public class ValidationUtils {
    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();
    private static final UrlValidator URL_VALIDATOR = UrlValidator.getInstance();

    private ValidationUtils() {
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_VALIDATOR.isValid(email);
    }

    public static boolean isValidUrl(String url) {
        return URL_VALIDATOR.isValid(url);
    }
}
