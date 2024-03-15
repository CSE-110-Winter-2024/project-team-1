package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

// android imports
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

// androidx imports
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

// internal imports
import java.util.Calendar;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.TaskFilterOption;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskInterval;
import edu.ucsd.cse110.successorator.lib.domain.TaskType;
import edu.ucsd.cse110.successorator.util.DateManager;

public class CreateTaskDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private FragmentDialogCreateTaskBinding view;
    private DateManager dateManager;

    CreateTaskDialogFragment(DateManager dateManager) {
        this.dateManager = dateManager;
    }

    public static CreateTaskDialogFragment newInstance(DateManager dateManager) {
        var fragment = new CreateTaskDialogFragment(dateManager);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateTaskBinding.inflate(getLayoutInflater());

        if (activityModel.getSelectedFilter() == TaskFilterOption.Recurring) {
            view.filterRadioGroup.setVisibility(View.VISIBLE);
            // default option is weekly
            view.weekly.toggle();
        }

        if (activityModel.getSelectedFilter() == TaskFilterOption.Today || activityModel.getSelectedFilter() == TaskFilterOption.Tomorrow) {
            view.filterRadioGroup.setVisibility(View.VISIBLE);
            // default option is weekly
            view.oneTime.toggle();
        }

        var dialog = new AlertDialog.Builder(getActivity())
                .setTitle("New Task")
                .setMessage("Please enter the new task's title.")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();

        if (activityModel.getSelectedFilter() == TaskFilterOption.Tomorrow) {
            view.weekly.setText("weekly on " + dateManager.getTomorrowFormattedDate("E"));

            int weekOfMonth = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) / 7 + 1;
            String ord = (weekOfMonth == 1) ? "st" : (weekOfMonth == 2) ? "nd" : (weekOfMonth == 3) ? "rd" : "th";
            view.monthly.setText("monthly on " + weekOfMonth + ord + dateManager.getTomorrowFormattedDate(" E"));
            view.yearly.setText("yearly on " + dateManager.getTomorrowFormattedDate("M/d"));
            view.editTextDate.setHint(dateManager.getTomorrowLongDate());
        } else {
            view.weekly.setText("weekly on " + dateManager.getFormattedDate("E"));

            int weekOfMonth = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1) / 7 + 1;
            String ord = (weekOfMonth == 1) ? "st" : (weekOfMonth == 2) ? "nd" : (weekOfMonth == 3) ? "rd" : "th";
            view.monthly.setText("monthly on " + weekOfMonth + ord + dateManager.getFormattedDate(" E"));
            view.yearly.setText("yearly on " + dateManager.getFormattedDate("M/d"));
            view.editTextDate.setHint(dateManager.getLongDate());
        }
        view.taskNameEntry.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                return true;
            }
            return false;
        });

        if (activityModel.getSelectedFilter() == TaskFilterOption.Recurring) {
            view.oneTime.setVisibility(View.GONE);
            view.editTextDate.setOnClickListener(v -> {
                var datePicker = DatePickerFragment.newInstance(view);
                datePicker.show(getParentFragmentManager(), "DatePickerFragment");
            });
        } else {
            view.editTextDate.setVisibility(View.GONE);
            view.startDateLabel.setVisibility(View.GONE);
        }
        if (activityModel.getSelectedFilter() == TaskFilterOption.Pending) {
            view.filterRadioGroup.setVisibility(View.GONE);
        }

        return dialog;
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        // name
        var name = view.taskNameEntry.getText().toString();
        if (name.trim().isEmpty()) {
            Toast.makeText(getContext(), "Please enter a task name", Toast.LENGTH_LONG).show();
            return;
        }

        // context
        TaskContext context;
        int selectedContext = view.contextRadioGroup.getCheckedRadioButtonId();

        if (selectedContext == R.id.home) {
            context = TaskContext.Home;
        }
        else if (selectedContext == R.id.school) {
            context = TaskContext.School;
        }
        else if (selectedContext == R.id.errands) {
            context = TaskContext.Errands;
        }
        else if (selectedContext == R.id.work) {
            context = TaskContext.Work;
        }
        else {
            Toast.makeText(getContext(), "Please select a context", Toast.LENGTH_LONG).show();
            return;
        }

        // interval
        TaskInterval interval = null;
        if (view.daily.isChecked()) {
            interval = TaskInterval.Daily;
        }
        else if (view.weekly.isChecked()) {
            interval = TaskInterval.Weekly;
        }
        else if (view.monthly.isChecked()) {
            interval = TaskInterval.Monthly;
        }
        else if (view.yearly.isChecked()) {
            interval = TaskInterval.Yearly;
        }

        // date
        String dateText;
        if (view.editTextDate.getText().toString().trim().isEmpty()) {
            dateText = view.editTextDate.getHint().toString();
            assert !dateText.isEmpty();
        }
        else {
            dateText = view.editTextDate.getText().toString();
        }
        long createDate = dateManager.getDateFromFormattedString(dateText) / (24*60*60*1000);

        if (activityModel.getSelectedFilter() == TaskFilterOption.Pending) {
            createDate = 0;
        }

        if (activityModel.getSelectedFilter() == TaskFilterOption.Recurring) {
            if (view.editTextDate.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Please select a start date", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (interval != null) {
            var recurringTask = new SuccessoratorRecurringTask(null, name, -1, createDate, 0, interval, context, -1, -1);
            activityModel.add(recurringTask);
        }
        else {
            var task = new SuccessoratorTask(null, name, -1, false, TaskType.Normal, createDate, context);
            activityModel.add(task);
        }
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }
}
