package com.ranchat.chatting.common.support;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;

public class IpAddressExtractor {
    private static final String X_FORWARDED_FOR = "x-forwarded-for";

    private IpAddressExtractor() {
    }

    public static String extract(HttpServletRequest request) {
        return Collections.list(request.getHeaders(X_FORWARDED_FOR))
            .stream()
            .findFirst()
            .map(ipAddress -> ipAddress.split(",")[0])
            .orElse(null);
    }
}
