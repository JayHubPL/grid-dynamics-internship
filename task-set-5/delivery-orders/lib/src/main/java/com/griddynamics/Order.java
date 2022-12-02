package com.griddynamics;

import java.util.EnumMap;
import java.util.List;

import com.griddynamics.ordersources.OrderSource;
import com.griddynamics.stateimpl.*;
import com.griddynamics.subscribers.Subscriber;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Order {
    
    private final int ID;
    private final OrderSource source;
    private final List<Product> products;
    private final EnumMap<OrderState.StateName, List<Subscriber>> subscribers;

    private OrderState state;

    public Order(int ID, OrderSource source, List<Product> products, EnumMap<OrderState.StateName, List<Subscriber>> subscribers) {
        this.ID = ID;
        this.source = source;
        this.products = products;
        this.subscribers = subscribers;
        log.info("[{}] Order created", ID);
        state = new Waiting(this.subscribers);
        state.notifySubscribers(this);
    }

    public void advance() {
        state = switch (state.getStateName()) {
            case WAITING -> new Preparation(subscribers);
            case PREPARATION -> new ReadyForDelivery(subscribers);
            case READY_FOR_DELIVERY -> new BeingDelivered(subscribers);
            case BEING_DELIVERED -> new Delivered(subscribers);
            default -> throw new IllegalStateException("Order has already been finished");
        };
        state.notifySubscribers(this);
    }

    public List<Product> getProducts() {
        return products;
    }

    public OrderState getState() {
        return state;
    }

    public int getID() {
        return ID;
    }

    public OrderSource getSource() {
        return source;
    }

}
