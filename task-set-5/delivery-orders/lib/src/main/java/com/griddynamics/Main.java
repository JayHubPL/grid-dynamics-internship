package com.griddynamics;

import com.griddynamics.ordersources.PhoneSource;
import com.griddynamics.ordersources.PyszneSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    private static final long POLLING_DELAY = 1000;

    public static void main(String[] args) throws InterruptedException {
        OrderIntake intake = new OrderIntake();
        intake.addOrderSources(new PyszneSource(), new PhoneSource(intake.getDeleter()));
        while (true) {
            log.info("Polling orders...");
            intake.pollOrders();
            Thread.sleep(POLLING_DELAY);
        }
    }

}
