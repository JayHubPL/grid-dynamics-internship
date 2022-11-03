package com.griddynamics.stateimpl;

import com.griddynamics.Order;
import com.griddynamics.OrderState;

public class Waiting implements OrderState {

    @Override
    public void notifySubscribers(Order order) {
        /*
         * Notified should be the any party responsible for
         * the preparation of the order.
         */
    }

    @Override
    public State stateEnum() {
        return State.WAITING;
    }
    
}
