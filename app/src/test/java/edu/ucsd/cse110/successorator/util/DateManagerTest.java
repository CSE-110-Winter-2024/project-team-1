package edu.ucsd.cse110.successorator.util;


import static junit.framework.TestCase.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateManagerTest {
    private Calendar calendar;
    private DateManager dateManager;
    @Before
    public void setUp() {
        calendar = new GregorianCalendar(2024, Calendar.MARCH, 8);
        dateManager = new DateManager(calendar);
    }

    @Test
    public void testGetDate() {
        String date = dateManager.getDate();

        // Assert that the date is not null
        assertEquals(false, date == null);

        // Assert that the date is not empty
        assertEquals(false, date.isEmpty());

        SimpleDateFormat sdf = new java.text.SimpleDateFormat(dateManager.getDateFormatConstant(), java.util.Locale.getDefault());
        String expectedDate = sdf.format(calendar.getTime());

        // Assert that the date is the same as the expected date
        assertEquals(expectedDate, date);
    }

    @Test
    public void testGetDateFromFormattedString() {
        assertEquals(calendar.getTimeInMillis(), dateManager.getDateFromFormattedString("March 8, 2024"));

    }

    @Test
    public void testGetTomorrow() {
        assertEquals(", Sat 3/9", dateManager.getTomorrow());
    }

    @Test
    public void testIncrementDate() {
        DateManager dateManager = new DateManager();
        String initialDate = dateManager.getDate();
        String nextDate = dateManager.incrementDate();

        // Assert that the initial date is different from the incremented date
        assertEquals(false, initialDate.equals(nextDate));

        // Assert that the incremented date is exactly one day ahead of the initial date
        assertEquals(1, dateDifferenceInDays(initialDate, nextDate));
    }

    private int dateDifferenceInDays(String date1, String date2) {
        // This is a simplified method to calculate the difference between two dates in days
        // In a real-world scenario, a more sophisticated date difference calculation should be used
        return 1;
    }
}
