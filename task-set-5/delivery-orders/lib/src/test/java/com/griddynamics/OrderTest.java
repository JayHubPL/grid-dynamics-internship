package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.griddynamics.OrderState.StateName;
import com.griddynamics.ordersources.OrderSource;
import com.griddynamics.stateimpl.BeingDelivered;
import com.griddynamics.stateimpl.Delivered;
import com.griddynamics.stateimpl.Preparation;
import com.griddynamics.stateimpl.ReadyForDelivery;
import com.griddynamics.stateimpl.Waiting;
import com.griddynamics.subscribers.Subscriber;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    @Mock
    private OrderSource mockedOrderSource;
    @Mock
    private EnumMap<OrderState.StateName, List<Subscriber>> mockedSubscribers;

    private Order order;

    @BeforeEach
    public void init() {
        doReturn(Collections.emptyList()).when(mockedSubscribers).getOrDefault(any(), any());
        order = new Order(0, mockedOrderSource, Collections.emptyList(), mockedSubscribers);
    }

    @Test
    public void constructor_StartingStage_ShouldBeWaiting() {
        assertEquals(StateName.WAITING, order.getState().getStateName());
    }

    @ParameterizedTest
    @MethodSource
    public void advance_CurrentStateCanAdvance_ShouldChangeToTheNextState(StateName currState, StateName nextStateName) {
        setOrderState(order, getStateFromName(currState));
        order.advance();
        assertEquals(nextStateName, order.getState().getStateName());
    }

    @Test
    public void advance_CurrentStateIsDelivered_ShouldThrow() {
        setOrderState(order, getStateFromName(StateName.DELIVERED));
        assertThrows(IllegalStateException.class, () -> {
            order.advance();
        });
    }

    private void setOrderState(Order order, OrderState state) {
        try {
            Field stateField = order.getClass().getDeclaredField("state");
            stateField.setAccessible(true);
            stateField.set(order, state);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private OrderState getStateFromName(StateName stateName) {
        return switch (stateName) {
            case BEING_DELIVERED -> new BeingDelivered(mockedSubscribers);
            case DELIVERED -> new Delivered(mockedSubscribers);
            case PREPARATION -> new Preparation(mockedSubscribers);
            case READY_FOR_DELIVERY -> new ReadyForDelivery(mockedSubscribers);
            case WAITING -> new Waiting(mockedSubscribers);
        };
    }

    private static Stream<Arguments> advance_CurrentStateCanAdvance_ShouldChangeToTheNextState() {
        return Stream.of(
            Arguments.of(StateName.WAITING, StateName.PREPARATION),
            Arguments.of(StateName.PREPARATION, StateName.READY_FOR_DELIVERY),
            Arguments.of(StateName.READY_FOR_DELIVERY, StateName.BEING_DELIVERED),
            Arguments.of(StateName.BEING_DELIVERED, StateName.DELIVERED)
        );
    } 

}
