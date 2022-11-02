package com.griddynamics.task1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mentor implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final transient String password;
    private final List<Intern> interns;

    public Mentor(String name, String password, Intern... interns) {
        this.name = name;
        this.password = password;
        this.interns = new ArrayList<>();
        addInterns(interns);
    }

    public void addInterns(Intern... interns) {
        this.interns.addAll(Arrays.asList(interns));
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<Intern> getInterns() {
        return interns;
    }

}
