package com.griddynamics;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.griddynamics.subscribers.Subscriber;

public abstract class OrderState {
    
    protected final List<Subscriber> subscribers;

    public OrderState(Map<StateName, List<Subscriber>> orderSubscribers) {
        if (orderSubscribers == null) {
            throw new IllegalArgumentException("orderSubscribers cannot be null");
        }
        this.subscribers = orderSubscribers.getOrDefault(getStateName(), Collections.emptyList());
    }

    /**
     * For each stage different parties should be notified
     * of the changed order state as well as the client.
     * @param order provides notified parties with the
     * additional order info in order to possibly
     * advance them in the future
     */
    public void notifySubscribers(Order order) {
        subscribers.forEach(s -> s.notifyAboutOrder(order));
    }

    /**
     * Gives an enum representation of the {@code OrderState}
     * implementing class.
     * @return {@code State} enum matching the state
     * implemementation
     */
    public abstract StateName getStateName();

    @Override
    public String toString() {
        return getStateName().name();
    }

    public enum StateName {
        WAITING,
        PREPARATION,
        READY_FOR_DELIVERY,
        BEING_DELIVERED,
        DELIVERED;
    }

}
