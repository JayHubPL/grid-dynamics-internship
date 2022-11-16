package com.griddynamics.stateimpl;

import java.util.List;
import java.util.Map;

import com.griddynamics.OrderState;
import com.griddynamics.subscribers.Subscriber;

/*
 * Client is informed about the start of order preparation by the kitchen.
 * Kichen should advance if it finished preparing the order.
 */

public class Preparation extends OrderState {

    public Preparation(Map<StateName, List<Subscriber>> orderSubscribers) {
        super(orderSubscribers);
    }

    @Override
    public StateName getStateName() {
        return OrderState.StateName.PREPARATION;
    }
    
}
