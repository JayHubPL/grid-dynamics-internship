package com.griddynamics.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;

public class Serde {
    
    public static <T extends Serializable> void serializeObject(T obj, Path path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(obj);
            oos.flush();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserializeObject(Path path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (T) ois.readObject();
        }
    }

    public static <T extends Serializable> byte[] serializeToByteArray(T obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos);) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        }
    }

}
