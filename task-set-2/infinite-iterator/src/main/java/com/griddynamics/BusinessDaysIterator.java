package com.griddynamics;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Iterator;

public class BusinessDaysIterator implements Iterator<LocalDate> {

    private LocalDate date;

    // starting date is non-inclusive
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
            return false;
        }
        return true;
    }

    @Override
    public LocalDate next() {
        date = getNextBusinessDay(date);
        return date;
    }

    private LocalDate getNextBusinessDay(LocalDate date) throws DateTimeException {
        return switch (date.getDayOfWeek()) {
            case FRIDAY -> date.plusDays(3);
            case SATURDAY -> date.plusDays(2);
            default -> date.plusDays(1);
        };
    }
    
}
