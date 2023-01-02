package com.griddynamics;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.primitives.Primitives;
import com.google.common.reflect.ClassPath;
import com.griddynamics.annotations.JsonAttribute;
import com.griddynamics.annotations.JsonSerializable;

// OPTIONAL
// TODO add cycle detection
// TODO add support for maps

public class Serializer {

    public String serializeToJson(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object to serialize cannot be null");
        }
        Class<?> clazz = obj.getClass();
        if (!clazz.isAnnotationPresent(JsonSerializable.class)) {
            throw new IllegalArgumentException("The class " + clazz.getName() + " is not annotated with JsonSerializable");
        }
        return serialize(obj);
    }

    public Set<Class<?>> getAllJsonSerializableClassesInPackage(String packageName) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader()).getAllClasses().stream()
            .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(packageName))
            .map(clazz -> clazz.load())
            .filter(clazz -> clazz.isAnnotationPresent(JsonSerializable.class))
            .collect(Collectors.toSet());
    }

    private String serialize(Object obj) {
        return switch (getJsonDataType(obj)) {
            case NULL -> "null";
            case NUMBER -> ((Number)obj).toString();
            case STRING -> "\"" + obj.toString().replaceAll("\\p{C}", "") + "\"";
            case BOOLEAN -> (boolean)obj ? "true" : "false";
            case ARRAY -> serializeArray(obj);
            case OBJECT -> serializeObject(obj);
        };
    }

    @SuppressWarnings("unchecked")
    private String serializeArray(Object obj) {
        Collection<Object> collection = obj.getClass().isArray() ? List.of((Object[])obj) : (Collection<Object>)obj;
        return "[ " + collection.stream()
            .filter(this::isSerializable)
            .map(o -> serialize(o))
            .collect(Collectors.joining(", ")) + " ]";
    }

    private String serializeObject(Object obj) {
        try {
            Map<String, String> jsonElements = new HashMap<>();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!(field.isAnnotationPresent(JsonAttribute.class)
                && isSerializable(field.get(obj)))) {
                    continue; // if not serializable or not marked for serialization, skip it
                }
                String jsonFieldName = field.getAnnotation(JsonAttribute.class).jsonFieldName();
                if (jsonFieldName.isEmpty()) {
                    jsonFieldName = field.getName();
                }
                jsonElements.put(jsonFieldName, serialize(field.get(obj)));
            }
            return "{ " + jsonElements.entrySet().stream()
                .map(e -> String.format("\"%s\": %s", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")) + " }";
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Serialization failed, " + e.getMessage());
        }
    }

    private boolean isSerializable(Object obj) {
        if (!getJsonDataType(obj).equals(JsonDataType.OBJECT)) {
            return true;
        }
        return obj.getClass().isAnnotationPresent(JsonSerializable.class);
    }

    private JsonDataType getJsonDataType(Object obj) {
        if (Objects.isNull(obj)) {
            return JsonDataType.NULL;
        }
        Class<?> clazz = obj.getClass();
        if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
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
