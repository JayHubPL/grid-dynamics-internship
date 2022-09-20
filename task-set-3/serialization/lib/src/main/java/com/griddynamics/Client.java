package com.griddynamics;

import java.io.Serializable;

public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String username;
    // private transient final String password;
    private final Double balance;
    private final Integer age;

    public Client(String username, Double balance, Integer age) {
        this.username = username;
        // this.password = password;
        this.balance = balance;
        this.age = age;
    }
    
    public String getUsername() {
        return username;
    }

    public Integer getAge() {
        return age;
    }

    public Double getBalance() {
        return balance;
    }

}
