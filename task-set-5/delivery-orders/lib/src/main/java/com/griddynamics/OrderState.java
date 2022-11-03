package com.griddynamics;

/*
 * Use of a State behavioral design pattern altering the unimplemented
 * behavior of the notifySubscribers() method.
 */
public interface OrderState {
    
    /**
     * For each stage different parties should be notified
     * of the changed order state AS WELL AS THE CLIENT.
     * @param order provides notified parties with the
     * additional order info, if implementation requires it
     */
    void notifySubscribers(Order order);

    /**
     * Gives an enum representation of the {@code OrderState}
     * implementing class.
     * @return {@code State} enum matching the interface
     * implemementation
     */
    State stateEnum();

    public enum State {
        WAITING,
        PREPARATION,
        READY_FOR_DELIVERY,
        BEING_DELIVERED,
        DELIVERED;
    }

}
