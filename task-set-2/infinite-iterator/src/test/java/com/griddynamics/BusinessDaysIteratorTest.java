package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BusinessDaysIteratorTest {
    
    @Test
    public void HasNext_DoesNotChangeInternalState() {
        LocalDate startingDate = LocalDate.of(2022, 9, 7); // Wednesday
        BusinessDaysIterator businessDayIterator = new BusinessDaysIterator(startingDate);

        businessDayIterator.hasNext();
        businessDayIterator.hasNext();
        businessDayIterator.hasNext();
        
        LocalDate nextBusinessDay = startingDate.plusDays(1); // Thursday

        assertTrue(businessDayIterator.hasNext());
        assertEquals(nextBusinessDay, businessDayIterator.next());
    }

    @Test
    public void HasNext_DateIsMAX_ReturnsFalse() {
        LocalDate startingDate = LocalDate.MAX;
        BusinessDaysIterator businessDayIterator = new BusinessDaysIterator(startingDate);

        assertFalse(businessDayIterator.hasNext());
    }

    @Test
    public void Next_StartingDateIsFriday_ReturnsMonday() {
        LocalDate startingDate = LocalDate.of(2022, 9, 9); // Friday
        assertEquals(DayOfWeek.FRIDAY, startingDate.getDayOfWeek());

        BusinessDaysIterator businessDayIterator = new BusinessDaysIterator(startingDate);

        LocalDate nextBusinessDay = startingDate.plusDays(3); // Monday
        assertEquals(DayOfWeek.MONDAY, nextBusinessDay.getDayOfWeek());

        assertTrue(businessDayIterator.hasNext());
        assertEquals(nextBusinessDay, businessDayIterator.next());
    }

    @Test
    public void Next_StartingDateIsSaturday_ReturnsMonday() {
        LocalDate startingDate = LocalDate.of(2022, 9, 10); // Saturday
        assertEquals(DayOfWeek.SATURDAY, startingDate.getDayOfWeek());

        BusinessDaysIterator businessDayIterator = new BusinessDaysIterator(startingDate);

        LocalDate nextBusinessDay = startingDate.plusDays(2); // Monday
        assertEquals(DayOfWeek.MONDAY, nextBusinessDay.getDayOfWeek());

        assertTrue(businessDayIterator.hasNext());
        assertEquals(nextBusinessDay, businessDayIterator.next());
    }

    @Test
    public void Next_StartingDateIsSunday_ReturnsMonday() {
        LocalDate startingDate = LocalDate.of(2022, 9, 11); // Sunday
        assertEquals(DayOfWeek.SUNDAY, startingDate.getDayOfWeek());

        BusinessDaysIterator businessDayIterator = new BusinessDaysIterator(startingDate);

        LocalDate nextBusinessDay = startingDate.plusDays(1); // Monday
        assertEquals(DayOfWeek.MONDAY, nextBusinessDay.getDayOfWeek());

        assertTrue(businessDayIterator.hasNext());
        assertEquals(nextBusinessDay, businessDayIterator.next());
    }

    @ParameterizedTest
    @CsvSource({"2022-09-05", "2022-09-06", "2022-09-07", "2022-09-08"}) // Monday - Thursday
    public void Next_StartingDateIsFromMondayToThursday_ReturnsCorrectlyNextDay(LocalDate startingDate) {
        BusinessDaysIterator businessDayIterator = new BusinessDaysIterator(startingDate);

        LocalDate nextBusinessDay = startingDate.plusDays(1);

        assertTrue(businessDayIterator.hasNext());
        assertEquals(nextBusinessDay, businessDayIterator.next());
    }
}
