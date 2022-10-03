package com.griddynamics.utils;

public class ByteOps {
    
    public static byte booleansToByte(boolean... bools) {
        if (bools.length > 8) {
            throw new IllegalArgumentException("Too many arguments. Maximum is 8");
        }
        byte mask = 1;
        byte b = 0;
        for (boolean flag : bools) {
            if (flag) {
                b |= mask;
            }
            mask <<= 1;
        }
        return b;
    }

    public static boolean[] byteToBooleans(byte b) {
        boolean[] bools = new boolean[Byte.SIZE];
        byte mask = 1;
        for (int i = 0; i < Byte.SIZE; i++) {
            bools[i] = (b & mask) != 0;
            mask <<= 1;
        }
        return bools;
    }

}
