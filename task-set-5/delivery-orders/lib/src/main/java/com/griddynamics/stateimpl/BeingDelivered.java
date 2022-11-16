package com.griddynamics.stateimpl;

import java.util.List;
import java.util.Map;

import com.griddynamics.OrderState;
import com.griddynamics.subscribers.Subscriber;

/*
 * Client should be informed that their order is being delivered.
 * Courier should advance when they deliver the order to the client.
 */

public class BeingDelivered extends OrderState {

    public BeingDelivered(Map<StateName, List<Subscriber>> orderSubscribers) {
        super(orderSubscribers);
    }

    @Override
    public StateName getStateName() {
        return OrderState.StateName.BEING_DELIVERED;
    }
    
}
