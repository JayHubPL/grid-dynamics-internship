package com.griddynamics.stateimpl;

import com.griddynamics.Order;
import com.griddynamics.OrderState;

public class ReadyForDelivery implements OrderState {

    @Override
    public void notifySubscribers(Order order) {
        /*
         * Notified should be every courier in some proximity to
         * to the party responsible for the order preparation.
         * Some kind of search is required to determine which
         * couriers are available and close enought to deliver
         * the order to the client in shortest time possible.
         */
    }

    @Override
    public State stateEnum() {
        return State.READY_FOR_DELIVERY;
    }
    
}
