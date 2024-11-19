package com.ranchat.chatting.common.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranchat.chatting.common.web.CommonObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new CommonObjectMapper();

    private JsonUtils() {}

    public static String stringify(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static <T> T parse(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        try {
            return (List)objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
        } catch (JsonProcessingException var3) {
            throw new RuntimeException(var3);
        }
    }
}