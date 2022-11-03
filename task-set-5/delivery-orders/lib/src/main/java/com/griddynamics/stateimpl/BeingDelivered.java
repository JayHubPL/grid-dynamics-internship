package com.griddynamics.stateimpl;

import com.griddynamics.Order;
import com.griddynamics.OrderState;

public class BeingDelivered implements OrderState {

    @Override
    public void notifySubscribers(Order order) {
        /*
         * Should periodically inform the client about
         * the whereabouts of the courier as well as the
         * predicted time for delivery.
         */
    }

    @Override
    public State stateEnum() {
        return State.BEING_DELIVERED;
    }
    
}
