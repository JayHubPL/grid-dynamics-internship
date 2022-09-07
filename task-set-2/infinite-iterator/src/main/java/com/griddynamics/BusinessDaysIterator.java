package com.griddynamics;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Iterator;

public class BusinessDaysIterator implements Iterator<LocalDate> {

    private LocalDate date;

    /**
     * Creates an infinite business day iterator, which skips Saturdays and Sundays,
     * starting from {@code startingDate} excliusive.
     * Iterator will finish if and only if the {@code LocalDate} format exceeds its maximum value.
     * @param startingDate - a date to start iteration from, non-inclusive
     */
    public BusinessDaysIterator(LocalDate startingDate) {
        if (startingDate == null) {
            throw new IllegalArgumentException("startingDate cannot be null");
        }
        date = startingDate;
    }

    @Override
    public boolean hasNext() {
        try {
            getNextBusinessDay(date);
        } catch (DateTimeException dateTimeException) {
            /* 
             * This exception is thrown and caught if and only if the next
             * business day exceeds LocalDate.MAX value.
             */
            return false;
        }
        return true;
    }

    @Override
    public LocalDate next() {
        date = getNextBusinessDay(date);
        return date;
    }

    /**
     * Finds next business day after specified date. Skips Saturdays and Sundays.
     * @param date - {@code LocalDate}, for which to find next business day
     * @return next business day after {@code date}
     * @throws DateTimeException when date exceeds {@code LocalDate.MAX}
     */
    private LocalDate getNextBusinessDay(LocalDate date) throws DateTimeException {
        return switch (date.getDayOfWeek()) {
            case FRIDAY -> date.plusDays(3);
            case SATURDAY -> date.plusDays(2);
            default -> date.plusDays(1);
        };
    }
    
}
