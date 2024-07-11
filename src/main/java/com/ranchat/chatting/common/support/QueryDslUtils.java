package com.ranchat.chatting.common.support;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import org.springframework.data.domain.Sort;

public class QueryDslUtils {
    private QueryDslUtils() {
    }

    public static OrderSpecifier<?> createColumnOrder(Path<?> path, Sort.Order order) {
        return new OrderSpecifier(convertOrder(order), path);
    }

    private static Order convertOrder(Sort.Order order) {
        return Order.valueOf(order.getDirection().name());
    }
}