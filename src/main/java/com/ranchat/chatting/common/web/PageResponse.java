package com.ranchat.chatting.common.web;

import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

public record PageResponse<T>(
    List<T> items,
    Integer page,
    Integer size,
    Long totalCount,
    Integer totalPage
) {
    public PageResponse(List<T> items,
                        Integer page,
                        Integer size,
                        Long totalCount) {
        this(items,
            page,
            size,
            totalCount,
            (int) Math.ceil((double) totalCount / size)
        );
    }

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    public static <T> PageResponse<T> single(T value) {
        return new PageResponse<>(List.of(value), 0, 1, 1L);
    }

    public static <T> PageResponse<T> empty(Class<T> clazz) {
        return new PageResponse<>(Collections.emptyList(), 0, 1, 0L);
    }

    public boolean isEmpty() {
        return this.items == null || this.items().isEmpty();
    }
}
