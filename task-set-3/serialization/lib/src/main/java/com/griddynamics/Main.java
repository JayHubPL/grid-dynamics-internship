package com.griddynamics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        // task1();
        // task2();
        task3();
    }

    public static void task3() throws IOException {
        User user = new User();
        byte[] bytes = user.serializeToByteArray();
        System.out.printf("bytes.length: %d\n", bytes.length);
        // 112 bytes
        // after implementing Externalizable
        // 46 bytes
    }

    public static void task2() throws FileNotFoundException, IOException, ClassNotFoundException {
        Client client = new Client("Hubert", 69.420, 22);
        // serializeObject(client, Path.of("client_before_age")); // Object serialized before the age field is added
        Client deserializedClient = (Client) deserializeObject(Path.of("client_before_age"));
        System.out.printf("client.age (did not exists on serialization): %d\ndeserializedClient.age: %s\n", client.getAge(), deserializedClient.getAge());
        // client.age: 22
        // deserializedClient.age: null
        
        // Renaming name to username
        System.out.printf("client.username (was name): %s\ndeserializedClient.username: %s\n", client.getUsername(), deserializedClient.getUsername());
        // client.username: Hubert
        // deserializedClient.username: null
        
        // Removed transient password field
        // Deserialization was successful
        // Removing a transient field does not break compatibility

        // Change balance to Double type from Integer
        System.out.printf("client.balance (was integer): %s\ndeserializedClient.balance: %s\n", client.getBalance(), deserializedClient.getBalance());
        // java.lang.ClassCastException cannot assign instance of java.lang.Integer to field com.griddynamics.Client.balance of type java.lang.Double in instance of com.griddynamics.Client
        // comaptibility was broken, UID should be changed after this change
    }

    public static void task1() throws FileNotFoundException, IOException, ClassNotFoundException {
        Mentor mentor = new Mentor("Alex", "Serbin");
        Intern intern1 = new Intern("Hubert", "Mazur", mentor);
        Intern intern2 = new Intern("Leonardo", "DiCaprio", mentor);
        Intern intern3 = new Intern("Britney", "Spears", mentor);
        mentor.addInterns(intern1, intern2, intern3);
        serializeObject(mentor, Path.of("alex"));
        Mentor deserializedMentor = (Mentor) deserializeObject(Path.of("alex"));
        mentor.compareWith(deserializedMentor);
        // NAME
        //   equals(): true
        //   ==: false
        // PASSWORD
        //   equals(): false
        //   ==: false
        // INTERS
        //   mentor.hashCode():
        //     1st: 824909230
        //     2nd: 1190654826
        //   mentor.hashCode():
        //     1st: 824909230
        //     2nd: 1190654826
        //   mentor.hashCode():
        //     1st: 824909230
        //     2nd: 1190654826
    }

    public static void serializeObject(Object obj, Path path) throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

    public static Object deserializeObject(Path path) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()));
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

}
