package com.ranchat.chatting.common.support;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.util.StringUtils;

public class ExceptionUtils {
    private ExceptionUtils() {
    }

    public static String getMostSpecificCauseMessage(Throwable original) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(original);
        Throwable closestCause = getClosestCause(original, rootCause);
        return StringUtils.hasText(closestCause.getMessage()) ? closestCause.getMessage() : closestCause.getClass().getSimpleName();
    }

    private static Throwable getClosestCause(Throwable original, Throwable rootCause) {
        return rootCause != null ? rootCause : original;
    }
}
