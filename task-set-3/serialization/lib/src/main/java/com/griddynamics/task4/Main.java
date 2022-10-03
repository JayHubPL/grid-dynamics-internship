package com.griddynamics.task4;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objenesis.instantiator.ObjectInstantiator;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Main {
    
    public static void main(String[] args) throws IOException {
        Kryo kryo = new Kryo();
        /*
         * Kryo uses and isntance of Kryo to provide all of the serialization tools.
         * Many global parameters can be set by calling specific methods on this object,
         * such as...
         */
        kryo.setReferences(true);
        kryo.setAutoReset(false);
        /*
         * By default refrences are disabled, which means Kryo wouldn't try to figure
         * out if it had previously serialized or deserialized the same object. This
         * could lead to endless cycles.
         */
        kryo.register(Shell.class);
        /*
         * Registering allows for serialization and deserialization of objects.
         * The order is important here as it must be kept while serializing and deserializng.
         * Register method assigns every class an ID, but user can provide one manually and
         * make the ordering obsolete.
         * This is to provide type safety.
         * If only using copy features of Kryo, registration can be disabled.
         */
        Registration slugRegistration = kryo.register(Slug.class);
        slugRegistration.setInstantiator(new ObjectInstantiator<Slug>() {
            public Slug newInstance() {
                return new Slug(null);
            }
        });
        /*
         * no-args constructors are required when using default instantiation strategy. By creating
         * custom instantiator for classes which don't/shouldn't have no-args constructor, we have
         * eliminated KryoException during deserialization. 'name' can still be final.
         */
        Slug slug = new Slug("Bob");
        Shell shell = new Shell("pink", slug);
        slug.setHomeShell(shell);
        try (Output output = new Output(new FileOutputStream("data.bin"))) {
            /*
            * Kryo has its own Output and Input classes. They implement AutoCloseable.
            */
            kryo.writeObject(output, slug);
            kryo.writeObject(output, shell);
            /*
             * We can serialize/deserialize multiple objects into/from the same file.
             */
        }
        Slug deserSlug;
        Shell deserShell;
        try (Input input = new Input(new FileInputStream("data.bin"))) {
            deserSlug = kryo.readObject(input, Slug.class);
            deserShell = kryo.readObject(input, Shell.class);
        }
        System.out.println(deserSlug);
        System.out.println(deserShell);
        /*
         * I am Bob living in pink shell.
         * Shell[color=pink, occupiedBy=I am Bob living in pink shell.]
         * 
         * References are restored!
         */
    }

}
