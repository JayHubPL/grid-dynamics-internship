package com.griddynamics;

import com.griddynamics.ordersources.PhoneSource;
import com.griddynamics.ordersources.PyszneSource;

public class Main {

    private static final long POLLING_DELAY = 5000;

    public static void main(String[] args) throws InterruptedException {
        OrderIntake intake = new OrderIntake();
        intake.addOrderSources(new PyszneSource(), new PhoneSource());
        while (true) {
            intake.pollOrders();
            Thread.sleep(POLLING_DELAY);
        }
    }

}
