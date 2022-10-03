package com.griddynamics.task2;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Client implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private final String username;
    // private final String password; (REMOVED)
    private final Double balance; // (CHANGED TYPE)
    private final Integer age; // (ADDED)

    public Client(String name, Double balance, Integer age) {
        this.username = name;
        this.balance = balance;
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public String getUsername() {
        return username;
    }

}
