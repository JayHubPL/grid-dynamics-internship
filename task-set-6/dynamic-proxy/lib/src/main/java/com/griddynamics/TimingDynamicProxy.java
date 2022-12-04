package com.griddynamics;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimingDynamicProxy implements InvocationHandler {

    private final Object target;
    private final List<String> measuredMethods;

    public TimingDynamicProxy(Object target, List<String> measuredMethodsNames) {
        this.target = target;
        measuredMethods = measuredMethodsNames;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (measuredMethods.contains(methodName)) {
            long startTime = System.nanoTime();
            Object result = method.invoke(target, args);
            long elapsed = System.nanoTime() - startTime;
            log.info("Executing {} on {} finished in {} ns", method.getName(), target.getClass(), elapsed);
            return result;
        }
        return method.invoke(target, args);
    }
    
}
