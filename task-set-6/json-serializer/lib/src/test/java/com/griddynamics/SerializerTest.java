package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.griddynamics.helperclasses.ContainsAllPrimitives;
import com.griddynamics.helperclasses.ContainsRenamedFields;
import com.griddynamics.helperclasses.ContainsUnmarkedFields;
import com.griddynamics.helperclasses.SimplePOJO;

public class SerializerTest {
    
    private Serializer serializer = new Serializer();

    @Test
    public void serialize_ObjectContainsCollection_ListOfPrimitives_SerializeCorrectly() {
        List<Integer> listOfPrimitives = List.of(1, 2, 3);
        SimplePOJO obj = new SimplePOJO(listOfPrimitives);
        String actual = serializer.serializeToJson(obj);
        String expected = "{ \"obj\": [ 1, 2, 3 ] }";
        assertEquals(expected, actual);
    }

    @Test
    public void serialize_ObjectContainsCollection_SetOfSerializablePOJOs_SerializeCorrectly() {
        Set<SimplePOJO> setOfPOJOs = new HashSet<>(List.of(new SimplePOJO("A"), new SimplePOJO(1)));
        SimplePOJO obj = new SimplePOJO(setOfPOJOs);
        String actual = serializer.serializeToJson(obj);
        String opt1 = "{ \"obj\": [ { \"obj\": \"A\" }, { \"obj\": 1 } ] }";
        String opt2 = "{ \"obj\": [ { \"obj\": 1 }, { \"obj\": \"A\" } ] }";
        assertTrue(actual.equals(opt1) || actual.equals(opt2));
    }

    @Test
    public void serialize_ObjectContainsCollection_QueueOfSerializableAndUnserializablePOJOs_SerializeOnlySerializable() {
        Queue<Object> queue = new ArrayDeque<>(List.of(new SimplePOJO("A"), new Object(), new SimplePOJO("B")));
        SimplePOJO obj = new SimplePOJO(queue);
        String actual = serializer.serializeToJson(obj);
        String expected = "{ \"obj\": [ { \"obj\": \"A\" }, { \"obj\": \"B\" } ] }";
        assertEquals(expected, actual);
    }

    @Test
    public void serialize_ObjectContainsCollection_CollectionIsEmpty_SerializeCorrectly() {
        SimplePOJO obj = new SimplePOJO(Collections.emptyList());
        String actual = serializer.serializeToJson(obj);
        String expected = "{ \"obj\": [  ] }";
        assertEquals(expected, actual);
    }

    @Test
    public void serialize_ObjectContainsArray_OfSerializableObjects_SerializeCorrectly() {
        SimplePOJO obj = new SimplePOJO(new Object[]{new SimplePOJO("A"), 5, "str"});
        String actual = serializer.serializeToJson(obj);
        String expected = "{ \"obj\": [ { \"obj\": \"A\" }, 5, \"str\" ] }";
        assertEquals(expected, actual);
    }

    @Test
    public void serialize_ObjectContainsArray_ArrayIsEmpty_SerializeCorrectly() {
        SimplePOJO obj = new SimplePOJO(new Object[0]);
        String actual = serializer.serializeToJson(obj);
        String expected = "{ \"obj\": [  ] }";
        assertEquals(expected, actual);
    }

    @Test
    public void serialize_ObjectContainsPrimitives_SerializeCorrectly() {
        String actual = serializer.serializeToJson(new ContainsAllPrimitives());
        String expected = "{ \"b\": 0, \"s\": 0, \"c\": \"\0\", \"d\": 0.0, \"bool\": false, \"f\": 0.0, \"i\": 0, \"l\": 0 }";
        assertEquals(expected, actual);
    }

    @Test
    public void serialize_ObjectContainsString_SerializeCorrectly() {
        String actual = serializer.serializeToJson(new SimplePOJO("text\"\\\b\f\n\r\t"));
        String expected = "{ \"obj\": \"text\"\\\b\f\n\r\t\" }";
        assertEquals(expected, actual);
    }

    @Test
    public void serialize_ObjectContainsNull_SerializeCorrectly() {
        String actual = serializer.serializeToJson(new SimplePOJO(null));
        String expected = "{ \"obj\": null }";
        assertEquals(expected, actual);
    }

    @Test
    public void serialize_ObjectContainsPOJOs_UnmarkedAndMarkedForSerialization_SerializeOnlyMarked() {
        String actual = serializer.serializeToJson(new ContainsUnmarkedFields());
        String expected = "{ \"marked\": null }";
        assertEquals(expected, actual);
    }

    @Test
    public void serialize_ObjectContainsPOJOs_NotRenamedAndRenamed_SerializeWithCorrectNames() {
        String actual = serializer.serializeToJson(new ContainsRenamedFields());
        String expected = "{ \"dontRenameMe\": null, \"renamed\": null }";
        assertEquals(expected, actual);
    }

    @Test
    public void serialize_ObjectDoesNotHave_JsonSerializableAnnotation_ShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {
            serializer.serializeToJson(Integer.valueOf(0));
        });
    }

    @Test
    public void serialize_ObjectIsNull_ShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {
            serializer.serializeToJson(null);
        });
    }

    @Test
    public void getAllJsonSerializableClassesInPackage_Print() throws IOException {
        serializer.getAllJsonSerializableClassesInPackage("com.griddynamics.helperclasses")
            .forEach(System.out::println);
    }

}
