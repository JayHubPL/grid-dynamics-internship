package com.griddynamics;

public interface OrderSource {

    /**
     * Provied information about the client making an order.
     * @return a {@code ClientInfo} instance containing info
     * on the client
     */
    ClientInfo getClientInfo();

}
