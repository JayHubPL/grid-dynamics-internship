package com.griddynamics;

import java.io.Serializable;

public class Intern implements Serializable {

    private final String name;
    private transient final String password;
    private final Mentor mentor;

    public Intern(String name, String password, Mentor mentor) {
        this.name = name;
        this.password = password;
        this.mentor = mentor;
    }

    public Mentor getMentor() {
        return mentor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mentor == null) ? 0 : mentor.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Intern other = (Intern) obj;
        if (mentor == null) {
            if (other.mentor != null)
                return false;
        } else if (!mentor.equals(other.mentor))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        return true;
    }

}
