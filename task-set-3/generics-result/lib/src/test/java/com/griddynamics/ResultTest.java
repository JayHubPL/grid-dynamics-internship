package com.griddynamics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("rawtypes")
class ResultTest {
    
    @Test
    public void of_RunnableDoesNotThrow_ReturnOk() {
        Runnable runnable = () -> {};
        Result result = Result.of(runnable);

        assertEquals(Ok.class, result.getClass());
    }

    @Test
    public void of_RunnableDoesThrow_ReturnErr() {
        Runnable runnable = () -> { throw new RuntimeException(); };
        Result result = Result.of(runnable);

        assertEquals(Err.class, result.getClass());
    }

    @Test
    public void map_WhenResultIsOk_MapValue() throws Exception {
        Result<String, ?> result = Result.ok("value").map((s) -> s + "!");

        assertEquals("value!", result.unwrap());
    }

    @Test
    public void map_WhenResultIsErr_DoNothing() throws Exception {
        Result result = Result.err(new Exception()).map((s) -> s + "!");

        assertEquals(Err.class, result.getClass());
    }

    @Test
    public void mapErr_WhenResultIsOk_DoNothing() {
        Result result = Result.ok("value").mapErr((e) -> new Exception(e));

        assertEquals(Ok.class, result.getClass());
    }

    @Test
    public void mapErr_WhenResultIsErr_MapException() {
        Result result = Result.err(new Exception()).mapErr((e) -> new RuntimeException(e));

        assertThrowsExactly(RuntimeException.class, () -> result.unwrap());
    }

    @Test
    public void flatMap_WhenResultIsOk_FlatMapsToErrByFactoryMethod_MapsToErr() {
        Result result = Result.ok("value").flatMap((r) -> Result.err(new Exception()));

        assertEquals(Err.class, result.getClass());
    }

    @Test
    public void flatMap_WhenResultIsErr_FlatMapsToOkByFactoryMethod_MapsToOk() throws Exception {
        Result result = Result.err(new Exception()).flatMap((r) -> Result.ok("value"));

        assertEquals(Ok.class, result.getClass());
    }

    @Test
    public void orElse_WhenResultIsOk_ReturnResultValue() {
        Result result = Result.ok("value");

        assertEquals("value", result.orElse("other"));
    }

    @Test
    public void orElse_WhenResultIsErr_ReturnOtherValue() {
        Result result = Result.err(null);

        assertEquals("other", result.orElse("other"));
    }

    @Test
    public void unwrap_WhenresultIsOk_ReturnResultValue() throws Exception {
        Result result = Result.ok("value");

        assertEquals("value", result.unwrap());
    }

    @Test
    public void unwrap_WhenresultIsErr_ThrowResultException() {
        Result result = Result.err(new Exception());

        assertThrows(Exception.class, () -> result.unwrap());
    }

}
