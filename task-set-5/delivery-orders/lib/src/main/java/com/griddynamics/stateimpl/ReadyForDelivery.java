package com.griddynamics.stateimpl;

import java.util.List;
import java.util.Map;

import com.griddynamics.OrderState;
import com.griddynamics.subscribers.Subscriber;

/*
 * Courier should be informed that they can pick up the order.
 * Client should know that the order has been prepared and awaits transport.
 * Courier should advance when they pick up the order and begin delivery.
 */

public class ReadyForDelivery extends OrderState {

    public ReadyForDelivery(Map<StateName, List<Subscriber>> orderSubscribers) {
        super(orderSubscribers);
    }

    @Override
    public StateName getStateName() {
        return OrderState.StateName.READY_FOR_DELIVERY;
    }
    
}
