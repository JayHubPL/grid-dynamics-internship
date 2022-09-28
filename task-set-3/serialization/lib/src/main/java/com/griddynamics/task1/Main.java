package com.griddynamics.task1;

import java.io.IOException;
import java.nio.file.Path;

import com.griddynamics.utils.Serde;

public class Main {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Mentor mentor = new Mentor("Alex", "Serbin");
        Intern intern1 = new Intern("Hubert", "Mazur", mentor);
        Intern intern2 = new Intern("Bartek", "Zadrozny", mentor);
        Intern intern3 = new Intern("Jakub", "Wasilewski", mentor);
        mentor.addInterns(intern1, intern2, intern3);
        Serde.serializeObject(mentor, Path.of("mentor.ser"));
        Mentor deserMentor = Serde.deserializeObject(Path.of("mentor.ser"));
        System.out.printf("%d %d%n", mentor.hashCode(), deserMentor.hashCode());
        /*
         * 1580066828 455659002
         * 
         * After default serialization and deserialization, the hashCode was not preserved.
         * This is beacuse by default the hashCode() returns an integer that represents the
         * internal memory address of the object. Deserialization creates new object.
         */
        System.out.printf("%s %s%n", mentor.getName(), deserMentor.getName());
        System.out.printf("%s %s%n", mentor.getPassword(), deserMentor.getPassword());
        /*
         * Alex Alex
         * Serbin Serbin
         * 
         * Values of String fields are preserved.
         */
        mentor.getInterns().stream().forEach(i -> System.out.println(i.hashCode()));
        deserMentor.getInterns().stream().forEach(i -> System.out.println(i.hashCode()));
        /*
         * 723074861 
         * 1950409828
         * 666988784
         * 
         * 2003749087
         * 1324119927
         * 990368553
         * 
         * Hash codes of interns in intern list after serialization was not preserved.
         */
        deserMentor.getInterns().stream().forEach(i -> System.out.println(i.getMentor().hashCode()));
        /* 
         * 455659002
         * 455659002
         * 455659002
         * 
         * Interns are cooreclty linked with the deserMentor instance of Mentor, not with the one before
         * serialization happened.
         */

        // CHANGES: password fields are now transient

        System.out.println(deserMentor.getPassword());
        /*
         * null
         * 
         * The password was not saved and was initilized with null.
         */
    }

}
