package com.griddynamics;

import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
// import java.io.Serializable;

public class User implements Externalizable {

    // bad code is for the sake of the task, do not do it in real life
    private boolean isActive;
    private boolean isAdmin;
    private boolean isModerator;
    private boolean isVIP;
    private boolean isMuted;
    private boolean isBanned;

    public byte[] serializeToByteArray() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos);) {
            oos.writeObject(this);
            return baos.toByteArray();
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        byte attributes = 0;
        attributes |= isActive ? 1 : 0;
        attributes |= isAdmin ? 2 : 0;
        attributes |= isModerator ? 4 : 0;
        attributes |= isVIP ? 8 : 0;
        attributes |= isMuted ? 16 : 0;
        attributes |= isBanned ? 32 : 0;
        out.writeByte(attributes);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        byte attributes = in.readByte();
        isActive = (attributes & 1) > 0;
        isAdmin =(attributes & 2) > 0;
        isModerator = (attributes & 4) > 0;
        isVIP = (attributes & 8) > 0;
        isMuted = (attributes & 16) > 0;
        isBanned = (attributes & 32) > 0;
    }
    
}
