package com.griddynamics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mentor implements Serializable {
    
    private String name;
    private transient final String password;
    private List<Intern> interns;

    public Mentor(String name, String password) {
        this.name = name;
        this.password = password;
        interns = new ArrayList<>();
    }

    public void addInterns(Intern... interns) {
        for (var intern : interns) {
            this.interns.add(intern);
        }
    }

    public void compareWith(Mentor o) {
        System.out.printf("NAME\n  equals(): %s\n  ==: %s\n", name.equals(o.name), name == o.name);
        System.out.printf("PASSWORD\n  equals(): %s\n  ==: %s\n", password.equals(o.password), password == o.password);
        System.out.println("INTERS");
        Iterator<Intern> iter1 = interns.iterator();
        Iterator<Intern> iter2 = o.interns.iterator();
        for (; iter1.hasNext() && iter2.hasNext(); ) {
            Intern intern1 = iter1.next();
            Intern intern2 = iter2.next();
            System.out.printf("  mentor.hashCode():\n    1st: %d\n    2nd: %d\n",
                intern1.getMentor().hashCode(), intern2.getMentor().hashCode()
            );
        }
    }

}
