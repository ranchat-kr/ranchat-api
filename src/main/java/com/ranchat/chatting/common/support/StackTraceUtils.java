package com.ranchat.chatting.common.support;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StackTraceUtils {
    private static final int defaultLimit = 5;

    private StackTraceUtils() {
    }

    public static String toString(Throwable e, int limit) {
        return Arrays.stream(e.getStackTrace())
            .limit(limit)
            .map(StackTraceElement::toString)
            .collect(Collectors.joining("\n"));
    }

    public static String toString(Throwable e) {
        return toString(e, defaultLimit);
    }
}
