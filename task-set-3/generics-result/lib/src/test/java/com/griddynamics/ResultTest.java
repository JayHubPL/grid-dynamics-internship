package com.griddynamics;

import org.junit.jupiter.api.Test;

import com.google.common.base.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
    
    @Test
    public void err_ArgumentIsNull_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Result.err(null));
    }

    @Test
    public void of_RunnableDoesNotThrow_ReturnOk() throws Exception {
        Supplier<String> supplier = () -> "value";
        Result<String, ?> result = Result.of(supplier);

        assertEquals(Ok.class, result.getClass());
        assertEquals("value", result.orElse(""));
    }

    @Test
    public void of_RunnableDoesThrow_ReturnErr() {
        Supplier<String> supplier = () -> { throw new RuntimeException(); };
        Result<String, ?> result = Result.of(supplier);

        assertEquals(Err.class, result.getClass());
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

        assertEquals(Err.class, result.getClass());
    }

    @Test
    public void mapErr_WhenResultIsOk_DoNothing() {
        Result<String, Exception> result = Result.ok("value").mapErr((e) -> new Exception(e));

        assertEquals(Ok.class, result.getClass());
    }

    @Test
    public void mapErr_WhenResultIsErr_MapException() {
        Result<?, RuntimeException> result = Result.err(new Exception()).mapErr((e) -> new RuntimeException(e));

        assertThrowsExactly(RuntimeException.class, () -> result.unwrap());
    }

    @Test
    public void flatMap_WhenResultIsOk_FlatMapsToErrByFactoryMethod_MapsToErr() {
        Result<String, ?> result = Result.ok("value").flatMap((r) -> Result.err(new Exception()));

        assertEquals(Err.class, result.getClass());
    }

    @Test
    public void flatMap_WhenResultIsErr_DoesNothing() throws Exception {
        Result<String, ?> result = Result.err(new Exception()).flatMap((r) -> Result.ok("value"));

        assertEquals(Err.class, result.getClass());
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
