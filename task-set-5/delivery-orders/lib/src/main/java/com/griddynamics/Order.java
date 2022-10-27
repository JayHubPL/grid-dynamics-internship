package com.griddynamics;

import java.util.List;

public class Order {
    
    private final long id;
    private final List<Product> products;
    private final String comments;
    private final OrderGiver orderGiver;
    private final List<OrderTaker> orderTakers;
    private OrderStatus status;

    public Order(long id, List<Product> products, String comments, OrderGiver orderGiver,
            List<OrderTaker> orderTakers) {
        this.id = id;
        this.products = products;
        this.comments = comments;
        this.orderGiver = orderGiver;
        this.orderTakers = orderTakers;
        status = OrderStatus.WAITING;
    }

    /**
     * Advances the order status by one step changing its status and returning it.
     * If order has already {@code DELIVERED} status, an exception is thrown.
     * @return new current status of the order
     */
    public OrderStatus nextStep() {
        status = switch (status) {
            case WAITING -> OrderStatus.PREPARATION;
            case PREPARATION -> OrderStatus.READY_FOR_DELIVERY;
            case READY_FOR_DELIVERY -> OrderStatus.BEING_DELIVERED;
            case BEING_DELIVERED -> OrderStatus.DELIVERED;
            case DELIVERED -> throw new IllegalStateException("Order has already been delivered");
        };
        return status;
    }

}
