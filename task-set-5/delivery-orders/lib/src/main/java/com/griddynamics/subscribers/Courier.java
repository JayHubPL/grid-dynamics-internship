package com.griddynamics.subscribers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.griddynamics.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Courier implements Subscriber {

    @Override
    public void notifyAboutOrder(Order order) {
        log.info("[{}] {}\t{}", order.getID(), getClass().getSimpleName(), order.getState());
        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            log.info("[{}] {} is advancing the order",  order.getID(), getClass().getSimpleName());
            order.advance();
        });
    }
    
}
