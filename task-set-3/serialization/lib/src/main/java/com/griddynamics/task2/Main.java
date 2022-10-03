package com.griddynamics.task2;

import java.io.IOException;
import java.nio.file.Path;

import com.griddynamics.utils.Serde;

public class Main {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = new Client("Hubert", 69.420, 22); // before change (1) age was not a field
                                                      // before change (3) password "Mazur" was provided
                                                      // before change (4) balance was Integer(69)
        Serde.serializeObject(client, Path.of("client_changed.ser")); // before change (1) was "client.ser"

        // (1) CHANGES: added 'private final Integer age' after serialization

        Client deserClient = Serde.deserializeObject(Path.of("client.ser"));
        System.out.println(deserClient.getAge());
        /*
         * null
         * 
         * Age couldn't be preserved because it didn't exist at the time the object was serialized.
         */

        // (2) CHANGES: renamed 'name' field to 'username'

        System.out.println(deserClient.getUsername());
        /*
         * null
         * 
         * After changing field's name, deserialization can't reproduce the value given before
         * the change.
         */

        // (3) CHANGES: removed 'password' field
        
        /*
         * Object is being deserialized correctly despite of removing a non-static/non-transient field
         */

        // (4) CHANGES: changed 'balance' type from Integer to Double

        /*
         * ClassCastException is thrown. Deserialization was not successful.
         */

        /*
         * Filed 'serialVersionUID' should have been changed when we:
         *  - renamed field 'name'
         *  - removed 'pasword' field
         *  - changed 'balance' type 
         */

    }

}
