package com.griddynamics.subscribers;

import com.griddynamics.Order;
import com.griddynamics.OrderIntake;

public class OrderDeleter implements Subscriber {

    private final OrderIntake orderIntake;

    public OrderDeleter(OrderIntake orderIntake) {
        this.orderIntake = orderIntake;
    }

    @Override
    public void notifyAboutOrder(Order order) {
        orderIntake.removeOrder(order);
    }
    
}
