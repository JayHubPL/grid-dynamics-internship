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
         * Using writeObject():
         * 52
         * 
         * New size of byte array is over half as small as the previous one.
         * The class of the object, the signature of the class, and the values of the non-transient and non-static
         * fields of the class and all of its supertypes are written.
         */

        /*
         * Using writeExternal():
         * 7
         *
         * Extremely small size of serialized object. Only bare minimum about object's state is serialized.
         */

        // CHANGES: User has now a 'server' field

        Server server = new Server(user);
        user.joinServer(server);
        bytes = Serde.serializeToByteArray(user);
        System.out.println(bytes.length);

        /*
         * 253
         * 
         * By having Server implement Serializable and User implement Externalizable, the serialization of
         * the user is finite and complete.
         */

        // CHANGES: Server implements Externalizable instead of Serializable

        /*
         * 227
         * 
         * Serialization was successful, the size was decreased.
         */
    }

}
