package com.griddynamics;

import java.util.List;

import com.griddynamics.stateimpl.*;

public class Order {
    
    private final int ID;
    private final OrderSource source;
    private final List<Product> products;
    private OrderState state;

    public Order(int ID, OrderSource source, List<Product> products) {
        this.ID = ID;
        this.source = source;
        this.products = products;
        state = new Waiting();
        state.notifySubscribers(this);
    }

    public void advance() {
        state = switch (state.stateEnum()) {
            case WAITING -> new Preparation();
            case PREPARATION -> new ReadyForDelivery();
            case READY_FOR_DELIVERY -> new BeingDelivered();
            case BEING_DELIVERED -> new Delivered();
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
