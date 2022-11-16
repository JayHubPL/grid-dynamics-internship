package com.griddynamics.stateimpl;

import java.util.List;
import java.util.Map;

import com.griddynamics.OrderState;
import com.griddynamics.subscribers.Subscriber;

/*
 * Kitchen and the client are informed about pending order.
 * Kitchen should advance if it is ready to start preparing the order.
 */

public class Waiting extends OrderState {

    public Waiting(Map<StateName, List<Subscriber>> orderSubscribers) {
        super(orderSubscribers);
    }

    @Override
    public StateName getStateName() {
        return OrderState.StateName.WAITING;
    }
    
}
