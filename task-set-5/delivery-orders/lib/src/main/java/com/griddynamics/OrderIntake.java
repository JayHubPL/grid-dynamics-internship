package com.griddynamics;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.griddynamics.ordersources.OrderSource;

public class OrderIntake {
    
    private final Set<OrderSource> orderSources = new HashSet<>();
    private final List<Order> orders = new LinkedList<>();

    public void addOrderSources(OrderSource... orderSources) {
        Collections.addAll(this.orderSources, orderSources);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }

    public void pollOrders() {
        orderSources.stream()
            .flatMap(os -> os.fetchWaitingOrders().stream())
            .forEach(this.orders::add);
    }

}
