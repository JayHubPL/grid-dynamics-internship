package com.griddynamics.subscribers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.griddynamics.Order;

public class Kitchen implements Subscriber {

    @Override
    public void notifyAboutTheOrder(Order order) {
        System.out.printf("[%d] %s\t%s\n", order.getID(), getClass().getSimpleName(), order.getState());
        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            System.out.printf("%s is advancing the order #%d\n", getClass().getSimpleName(), order.getID());
            order.advance();
        });
    }
    
}
