package com.griddynamics;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.primitives.Primitives;
import com.griddynamics.annotations.JsonAttribute;
import com.griddynamics.annotations.JsonSerializable;

// TODO add cycle detection

public class Serializer {

    public String serialize(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object to serialize cannot be null");
        }
        Class<?> clazz = obj.getClass();
        if (!clazz.isAnnotationPresent(JsonSerializable.class)) {
            throw new IllegalArgumentException("The class " + clazz.getSimpleName() + " is not annotated with JsonSerializable");
        }
        return serializeObj(obj);
    }

    private String serializeObj(Object obj) {
        return switch (getJsonDataType(obj)) {
            case NULL -> "null";
            case NUMBER -> ((Number)obj).toString();
            case STRING -> "\"" + obj + "\"";
            case BOOLEAN -> (boolean)obj ? "true" : "false";
            case ARRAY -> "[ " + Arrays.stream((Object[])obj)
                .map(o -> serializeObj(o))
                .collect(Collectors.joining(", ")) + " ]";
            case OBJECT -> {
                try {
                    Map<String, String> jsonElements = new HashMap<>();
                    for (Field field : obj.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        String jsonFieldName = field.getName();
                        if (field.isAnnotationPresent(JsonAttribute.class)) {
                            jsonFieldName = field.getAnnotation(JsonAttribute.class).jsonFieldName();
                        }
                        if (getJsonDataType(field.get(obj)).equals(JsonDataType.OBJECT) &&
                            !field.getType().isAnnotationPresent(JsonSerializable.class)) {
                            continue; // if inner POJO is not serializable, skip it
                        }
                        jsonElements.put(jsonFieldName, serializeObj(field.get(obj)));
                    }
                    yield "{ " + jsonElements.entrySet().stream()
                        .map(e -> String.format("\"%s\": %s", e.getKey(), e.getValue()))
                        .collect(Collectors.joining(", ")) + " }";
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException("Serialization failed, " + e.getMessage());
                }
            }
        };
    }

    private JsonDataType getJsonDataType(Object obj) {
        if (Objects.isNull(obj)) {
            return JsonDataType.NULL;
        }
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            return JsonDataType.ARRAY;
        }
        if (clazz.equals(String.class) || Primitives.wrap(clazz).equals(Character.class)) {
            return JsonDataType.STRING;
        }
        if (Primitives.wrap(clazz).equals(Boolean.class)) {
            return JsonDataType.BOOLEAN;
        }
        if (Number.class.isAssignableFrom(Primitives.wrap(clazz))) {
            return JsonDataType.NUMBER;
        }
        return JsonDataType.OBJECT;
    }

}
