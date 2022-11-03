package com.griddynamics.stateimpl;

import com.griddynamics.Order;
import com.griddynamics.OrderState;

public class Preparation implements OrderState {

    @Override
    public void notifySubscribers(Order order) {
        /*
         * Only client should be notified.
         */
    }

    @Override
    public State stateEnum() {
        return State.PREPARATION;
    }
    
}
