package com.ranchat.chatting.common;

import java.lang.reflect.Field;

public class FixtureReflectionUtils {

    public static void reflect(Object obj, Object fixture) {
        try {
            for (var fixtureField : fixture.getClass().getDeclaredFields()) {
                var field = getField(obj, obj.getClass(), fixtureField.getName());
                field.setAccessible(true);
                fixtureField.setAccessible(true);
                field.set(obj, fixtureField.get(fixture));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setField(Object obj, String fieldName, Object value) {
        try {
            var field = getField(obj, obj.getClass(), fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Field getField(Object obj, Class<?> clazz, String fieldName) {
        try {
            if (clazz == Object.class) {
                throw new IllegalArgumentException("해당 필드를 찾을 수 없습니다. fieldName: " + fieldName);
            }

            var field = clazz.getDeclaredField(fieldName);
            return field;
        } catch (NoSuchFieldException e) {
            if (obj.getClass().getSuperclass() == Object.class) {
                throw new RuntimeException(e);
            }

            return getField(obj, clazz.getSuperclass(), fieldName);
        }
    }
}
