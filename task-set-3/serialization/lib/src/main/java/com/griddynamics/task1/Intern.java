package com.griddynamics.task1;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Intern implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private final String name;
    private final transient String password;
    private final Mentor mentor;

    public Intern(String name, String password, Mentor mentor) {
        this.name = name;
        this.password = password;
        this.mentor = mentor;
    }

    public Mentor getMentor() {
        return mentor;
    }

}
