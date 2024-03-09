package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.SuperscriptSpan;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.successorator.util.DateManager;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private FragmentDialogCreateTaskBinding view;
    DatePickerFragment(FragmentDialogCreateTaskBinding view) {
        this.view = view;
    }
    public static DatePickerFragment newInstance(FragmentDialogCreateTaskBinding view) {
        var fragment = new DatePickerFragment(view);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        var datePicker = new DatePickerDialog(requireContext(), this, year, month, day);
        datePicker.getDatePicker().setMinDate(date.getTimeInMillis());
        return datePicker;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        var dateManager = new DateManager(calendar);
        this.view.editTextDate.setText(dateManager.getLongDate());

        String weeklyText = "weekly on " + dateManager.getFormattedDate("E");
        this.view.weekly.setText(weeklyText);

        int weekOfMonth = (dayOfMonth - 1) / 7 + 1;
        String ord = (weekOfMonth == 1) ? "st" : (weekOfMonth == 2) ? "nd" : (weekOfMonth == 3) ? "rd" : "th";
        SpannableString monthlyText = new SpannableString("monthly on " +
                weekOfMonth + ord + dateManager.getFormattedDate(" E"));
        // superscript ord
        monthlyText.setSpan(new SuperscriptSpan(), 12, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.view.monthly.setText(monthlyText);

        String yearlyText = "yearly on " + dateManager.getFormattedDate("M/d");
        this.view.yearly.setText(yearlyText);
    }
}
