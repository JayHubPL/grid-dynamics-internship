package com.griddynamics.subscribers;

import com.griddynamics.ClientInfo;
import com.griddynamics.Order;

public class Client implements Subscriber {

    private final ClientInfo clientInfo;

    public Client(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public void notifyAboutTheOrder(Order order) {
        System.out.printf("[%d] %s\t%s\n", order.getID(), getClass().getSimpleName(), order.getState());
    }
    
    public ClientInfo getClientInfo() {
        return clientInfo;
    }
}
