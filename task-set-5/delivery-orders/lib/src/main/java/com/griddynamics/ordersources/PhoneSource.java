package com.griddynamics.ordersources;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.griddynamics.ClientInfo;
import com.griddynamics.Order;
import com.griddynamics.OrderState.StateName;
import com.griddynamics.subscribers.Client;
import com.griddynamics.subscribers.Courier;
import com.griddynamics.subscribers.Kitchen;
import com.griddynamics.subscribers.OrderDeleter;
import com.griddynamics.subscribers.Subscriber;

/*
 * An EXAMPLE of OrderSource implementation for Phone method of ordering.
 * It sends 3 orders with no products when asked for the first time. After that
 * no new orders are being supplied. In real application this fetching would be
 * achieved e.g by manually adding order data in an GUI app.
 */

public class PhoneSource implements OrderSource {

    private final OrderDeleter deleter;
    private boolean sentOnce = false;

    public PhoneSource(OrderDeleter deleter) {
        this.deleter = deleter;
    }

    @Override
    public List<Order> fetchWaitingOrders() {
        List<Order> orders = Collections.emptyList();
        if (!sentOnce) {
            sentOnce = true;
            orders = generateOrders(3, getMappedSubscribers());
        }
        return orders;
    }

    private EnumMap<StateName, List<Subscriber>> getMappedSubscribers() {
        Client client = new Client(new ClientInfo("Hubert", "Mazur", "Address"));
        Kitchen kitchen = new Kitchen();
        Courier courier = new Courier();
        var subs = new EnumMap<StateName, List<Subscriber>>(StateName.class);
        subs.put(StateName.WAITING, List.of(kitchen, client));
        subs.put(StateName.PREPARATION, List.of(kitchen, client));
        subs.put(StateName.READY_FOR_DELIVERY, List.of(courier, client));
        subs.put(StateName.BEING_DELIVERED, List.of(courier, client));
        subs.put(StateName.DELIVERED, List.of(deleter, client));
        return subs;
    }

    private List<Order> generateOrders(int n, EnumMap<StateName, List<Subscriber>> subs) {
        return IntStream.range(0, n)
            .mapToObj(i -> new Order(i, this, Collections.emptyList(), subs))
            .collect(Collectors.toList());
    }
    
}
