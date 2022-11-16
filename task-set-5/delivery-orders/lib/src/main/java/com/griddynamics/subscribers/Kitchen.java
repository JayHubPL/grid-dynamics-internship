package com.griddynamics.subscribers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.griddynamics.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Kitchen implements Subscriber {

    @Override
    public void notifyAboutTheOrder(Order order) {
        log.info(String.format("[%d] %s\t%s", order.getID(), getClass().getSimpleName(), order.getState()));
        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            log.info(String.format("[%d] %s is advancing the order",  order.getID(), getClass().getSimpleName()));
            order.advance();
        });
    }
    
}
