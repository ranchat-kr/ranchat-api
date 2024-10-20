package com.ranchat.chatting.common.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.util.StringUtils;

@Slf4j
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

    public static void ignoreException(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.error("Exception has ignored // error message: {}", e.getMessage(), e);
            // ignore
        }
    }
}
