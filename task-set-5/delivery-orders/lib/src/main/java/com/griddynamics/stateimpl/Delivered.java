package com.griddynamics.stateimpl;

import java.util.List;
import java.util.Map;

import com.griddynamics.OrderState;
import com.griddynamics.subscribers.Subscriber;

/*
 * Client should be informed that the order was delivered.
 * No advancing required.
 */

public class Delivered extends OrderState {

    public Delivered(Map<StateName, List<Subscriber>> orderSubscribers) {
        super(orderSubscribers);
    }

    @Override
    public StateName getStateName() {
        return OrderState.StateName.DELIVERED;
    }
    
}
