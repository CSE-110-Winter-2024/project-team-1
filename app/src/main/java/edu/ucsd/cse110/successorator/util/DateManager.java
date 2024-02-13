package edu.ucsd.cse110.successorator.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateManager {
    private static final String DATE_FORMAT = "EEEE MMMM dd";

    private Calendar calendar = Calendar.getInstance();

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public String incrementDate() {
        // Increment the date by 1
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        // Format the new date
        return sdf.format(calendar.getTime());
    }
}
