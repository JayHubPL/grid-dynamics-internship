package com.griddynamics.task3;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.griddynamics.utils.ByteOps;

public class User implements Externalizable {

    private static final long serialVersionUID = 1L;
    private Server server;

    private boolean isActive;
    private boolean isAdmin;
    private boolean isModerator;
    private boolean isVIP;
    private boolean isMuted;
    private boolean isBanned;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(ByteOps.booleansToByte(isActive, isAdmin, isModerator, isVIP, isMuted, isBanned));
        out.writeObject(server);
        out.flush();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        boolean[] flags = ByteOps.byteToBooleans(in.readByte());
        isActive = flags[0];
        isAdmin = flags[1];
        isModerator = flags[2];
        isVIP = flags[3];
        isMuted = flags[4];
        isBanned = flags[5];
    }

    public void joinServer(Server server) {
        this.server = server;
    }
    
}
