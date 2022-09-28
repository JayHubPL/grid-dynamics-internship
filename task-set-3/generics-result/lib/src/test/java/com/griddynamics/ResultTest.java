package com.griddynamics;

import org.junit.jupiter.api.Test;

import com.google.common.base.Supplier;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;

class ResultTest {

    public static Class<?> okClass;
    public static Class<?> errClass;

    @BeforeAll 
    public static void init() throws ClassNotFoundException {
        okClass = Class.forName("com.griddynamics.Result$Ok");
        errClass = Class.forName("com.griddynamics.Result$Err");
    }
    
    @Test
    public void err_ArgumentIsNull_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Result.err(null));
    }

    @Test
    public void of_RunnableDoesNotThrow_ReturnOk() throws Exception {
        Supplier<String> supplier = () -> "value";
        Result<String, ?> result = Result.of(supplier);

        assertInstanceOf(okClass, result);
        assertEquals("value", result.orElse(""));
    }

    @Test
    public void of_RunnableDoesThrow_ReturnErr() {
        Supplier<String> supplier = () -> { throw new RuntimeException(); };
        Result<String, ?> result = Result.of(supplier);

        assertInstanceOf(errClass, result);
        assertThrows(RuntimeException.class, () -> result.unwrap());
    }

    @Test
    public void map_WhenResultIsOk_MapValue() throws Exception {
        Result<String, Exception> result = Result.ok("value").map((s) -> s + "!");

        assertEquals("value!", result.unwrap());
    }

    @Test
    public void map_WhenResultIsErr_DoNothing() throws Exception {
        Result<String, Exception> result = Result.err(new Exception()).map((s) -> s + "!");

        assertInstanceOf(errClass, result);
    }

    @Test
    public void mapErr_WhenResultIsOk_DoNothing() {
        Result<String, Exception> result = Result.ok("value").mapErr((e) -> new Exception(e));

        assertInstanceOf(okClass, result);
    }

    @Test
    public void mapErr_WhenResultIsErr_MapException() {
        Result<?, RuntimeException> result = Result.err(new Exception()).mapErr((e) -> new RuntimeException(e));

        assertThrowsExactly(RuntimeException.class, () -> result.unwrap());
    }

    @Test
    public void flatMap_WhenResultIsOk_FlatMapsToErrByFactoryMethod_MapsToErr() {
        Result<String, ?> result = Result.ok("value").flatMap((r) -> Result.err(new Exception()));

        assertInstanceOf(errClass, result);
    }

    @Test
    public void flatMap_WhenResultIsErr_DoesNothing() throws Exception {
        Result<String, ?> result = Result.err(new Exception()).flatMap((r) -> Result.ok("value"));

        assertInstanceOf(errClass, result);
    }

    @Test
    public void orElse_WhenResultIsOk_ReturnResultValue() {
        Result<String, Exception> result = Result.ok("value");

        assertEquals("value", result.orElse("other"));
    }

    @Test
    public void orElse_WhenResultIsErr_ReturnOtherValue() {
        Result<String, Exception> result = Result.err(new Exception());

        assertEquals("other", result.orElse("other"));
    }

    @Test
    public void unwrap_WhenresultIsOk_ReturnResultValue() throws Exception {
        Result<String, Exception> result = Result.ok("value");

        assertEquals("value", result.unwrap());
    }

    @Test
    public void unwrap_WhenresultIsErr_ThrowResultException() {
        Result<?, Exception> result = Result.err(new Exception());

        assertThrows(Exception.class, () -> result.unwrap());
    }

}
