package com.griddynamics.subscribers;

import com.griddynamics.Order;

public interface Subscriber {
    
    /**
     * This method is used to communicate with subscribers and inform them about
     * the change in order status. It also provides {@code Order} object which
     * can be used to advance the order using {@code advance()} method.
     * @param order is a reference to the updated order
     */
    void notifyAboutTheOrder(Order order);

}
