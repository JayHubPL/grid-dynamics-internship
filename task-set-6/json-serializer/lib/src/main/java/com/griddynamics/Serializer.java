package com.griddynamics;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ClassUtils;

import com.google.common.primitives.Primitives;
import com.griddynamics.annotations.JsonAttribute;
import com.griddynamics.annotations.JsonSerializable;

// TODO primitive numbers and wrappers shouldn't use '"' symbol
// TODO byte type must be converted to string or number base 10
// TODO bools shouldn't use '"' symbol
// TODO null values should be supported as field values
// TODO add array support
// TODO add cycle detection
// TODO some string characters should be escaped using '\' symbol
// TODO ask if inner POJOs should have @JsonSerializable

// JSON types
// string.
// number.
// boolean.
// null.
// object.
// array.

public class Serializer {
    String str = "string";
    public static String serialize(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object to serialize cannot be null");
        }
        Class<?> clazz = obj.getClass();
        if (!clazz.isAnnotationPresent(JsonSerializable.class)) {
            throw new IllegalArgumentException("The class " + clazz.getSimpleName() + " is not annotated with JsonSerializable");
        }
        Map<String, String> jsonElements = new HashMap<>();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String jsonFieldName = field.getName();
                if (field.isAnnotationPresent(JsonAttribute.class)) {
                    jsonFieldName = field.getAnnotation(JsonAttribute.class).jsonFieldName();
                }
                Class<?> fieldClazz = field.getType();
                String jsonFieldValue = "\"" + field.get(obj).toString() + "\"";     
                if (!ClassUtils.isPrimitiveOrWrapper(fieldClazz) && !fieldClazz.equals(String.class)) { // field is inner POJO
                    if (!fieldClazz.isAnnotationPresent(JsonSerializable.class)) {
                        continue; // if inner POJO is not serializable, skip it
                    }
                    jsonFieldValue = serialize(field.get(obj));
                }
                jsonElements.put(jsonFieldName, jsonFieldValue);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Serialization failed, " + e.getMessage());
        }
        return "{ " + jsonElements.entrySet().stream()
            .map(e -> String.format("\"%s\": %s", e.getKey(), e.getValue()))
            .collect(Collectors.joining(", ")) + " }";
    }

}
