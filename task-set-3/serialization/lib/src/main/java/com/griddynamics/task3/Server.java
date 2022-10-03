package com.griddynamics.task3;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.List;

public class Server implements Externalizable {

    private List<User> users;

    public Server(User... users) {
        this.users = Arrays.asList(users);
    }

    public void addUsers(User user) {
        users.add(user);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(users);
        out.flush();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        users = (List<User>) in.readObject();
    }
    
}
