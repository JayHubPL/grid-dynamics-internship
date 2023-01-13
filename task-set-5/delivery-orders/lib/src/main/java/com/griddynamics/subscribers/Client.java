package com.griddynamics.subscribers;

import com.griddynamics.ClientInfo;
import com.griddynamics.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client implements Subscriber {

    private final ClientInfo clientInfo;

    public Client(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public void notifyAboutOrder(Order order) {
        log.info("[{}] {}\t{}", order.getID(), getClass().getSimpleName(), order.getState());
    }
    
    public ClientInfo getClientInfo() {
        return clientInfo;
    }
}
