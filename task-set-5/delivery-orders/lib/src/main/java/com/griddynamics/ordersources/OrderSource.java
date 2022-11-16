package com.griddynamics.ordersources;

import java.util.List;

import com.griddynamics.Order;

public interface OrderSource {

    /**
     * This method is used to obtain any orders that are waiting
     * to be processed. When there are no orders, an empty {@code List}
     * should be provided.
     * @return a {@code List<Order>} containing orders that should
     * be processed
     */
    List<Order> fetchWaitingOrders();

}
