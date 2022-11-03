package com.griddynamics.stateimpl;

import com.griddynamics.Order;
import com.griddynamics.OrderState;

public class Delivered implements OrderState {

    @Override
    public void notifySubscribers(Order order) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public State stateEnum() {
        return State.DELIVERED;
    }
    
}
