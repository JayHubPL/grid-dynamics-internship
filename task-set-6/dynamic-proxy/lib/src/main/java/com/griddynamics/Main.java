package com.griddynamics;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        List<String> measuredMethodsNames = List.of("put", "get");
        var hashMapProxy = TimingDynamicProxyMapFactory.<Integer, Integer>getHashMapProxy(measuredMethodsNames);
        var treeMapProxy = TimingDynamicProxyMapFactory.<Integer, Integer>getTreeMapProxy(measuredMethodsNames);
        var linkedHashMapProxy = TimingDynamicProxyMapFactory.<Integer, Integer>getLinkedHashMapProxy(measuredMethodsNames);
        int noOfElements = 10;
        List.of(hashMapProxy, treeMapProxy, linkedHashMapProxy).forEach(map -> {
            putElements(noOfElements, map);
            getElements(noOfElements, map);
        });
    }

    public static void putElements(int elements, Map<Integer, Integer> map) {
        IntStream.range(0, elements).forEach(i -> map.put(i, i));
    }

    public static void getElements(int elements, Map<Integer, Integer> map) {
        IntStream.range(0, elements).forEach(i -> map.get(i));
    }

}
