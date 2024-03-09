package edu.ucsd.cse110.successorator.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateManager {
    private static final String DATE_FORMAT = "EEEE M/d";
    private static final String LONG_DATE_FORMAT = "MMMM d, yyyy";

    private Calendar calendar = Calendar.getInstance();

    public DateManager() {
        calendar.add(Calendar.HOUR, -2);
    }

    // mocking
    public DateManager(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getDate() {
        return getFormattedDate(DATE_FORMAT);
    }

    public String getLongDate() {
        return getFormattedDate(LONG_DATE_FORMAT);
    }

    public String getFormattedDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public long getDateFromFormattedString(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_FORMAT, Locale.getDefault());
        try {
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public String incrementDate() {
        // Increment the date by 1
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        // Format the new date
        return sdf.format(calendar.getTime());
    }

    public String getDateFormatConstant() {
        return DATE_FORMAT;
    }
}
