package com.griddynamics.task3;

import java.io.IOException;

import com.griddynamics.utils.Serde;

public class Main {
    
    public static void main(String[] args) throws IOException {
        User user = new User();
        byte[] bytes = Serde.serializeToByteArray(user);
        System.out.println(bytes.length);
        /*
         * 118
         * 
         * This is the number of bytes after default serialization
         */

        // CHANGES: User now implements Externalizable and has custom serialization implemented

        /*
         * 52
         * 
         * New size of byte array is over half as small as the previous one
         */

    }

}
