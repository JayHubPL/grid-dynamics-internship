package com.griddynamics;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("unchecked")
public class TimingDynamicProxyMapFactory {
    
    public static <K, V> Map<K, V> getHashMapProxy(List<String> targetMethodsNames) {
        return (Map<K, V>) Proxy.newProxyInstance(
            TimingDynamicProxy.class.getClassLoader(), 
            new Class[] { Map.class }, 
            new TimingDynamicProxy(new HashMap<K, V>(), targetMethodsNames));
    }

    public static <K, V> Map<K, V> getLinkedHashMapProxy(List<String> targetMethodsNames) {
        return (Map<K, V>) Proxy.newProxyInstance(
            TimingDynamicProxy.class.getClassLoader(), 
            new Class[] { Map.class }, 
            new TimingDynamicProxy(new LinkedHashMap<K, V>(), targetMethodsNames));
    }
    
    public static <K, V> Map<K, V> getTreeMapProxy(List<String> targetMethodsNames) {
        return (Map<K, V>) Proxy.newProxyInstance(
            TimingDynamicProxy.class.getClassLoader(), 
            new Class[] { Map.class }, 
            new TimingDynamicProxy(new TreeMap<K, V>(), targetMethodsNames));
    }

}
