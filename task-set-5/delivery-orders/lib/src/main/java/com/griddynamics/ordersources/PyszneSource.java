package com.griddynamics.ordersources;

import java.util.Collections;
import java.util.List;

import com.griddynamics.Order;

public class PyszneSource implements OrderSource {

    @Override
    public List<Order> fetchWaitingOrders() {
        return Collections.emptyList();
    }
    
}
