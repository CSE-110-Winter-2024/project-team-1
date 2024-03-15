package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

// android imports
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

// androidx imports
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

// internal imports
import java.time.LocalDate;
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
    private DateManager dateManager = new DateManager();

    CreateTaskDialogFragment() {
        // limitations of object oriented programming :(
    }

    public static CreateTaskDialogFragment newInstance() {
        var fragment = new CreateTaskDialogFragment();
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
        var name = view.taskNameEntry.getText().toString();

        if (name.trim().isEmpty()) {
            Toast.makeText(getContext(), "Please enter a task name", Toast.LENGTH_LONG).show();
            return;
        }

        // it doesn't really matter what we pass here, since the add method will extract
        // only the name from the task (consider changing? possible refactor could be helpful)
        long createDate = 0;
        long dueDate = 0;
        TaskInterval taskInterval = TaskInterval.Daily;
        switch (activityModel.getSelectedFilter()) {
            case Today:
                dueDate = LocalDate.now().toEpochDay();
                createDate = dueDate;
                break;
            case Tomorrow:
                dueDate = LocalDate.now().plusDays(1).toEpochDay();
                createDate = dueDate;
                break;
            case Pending:
                //if pending leave due date as 0, since it has no due date
                break;
            case Recurring:
                // simple validation
                if (view.editTextDate.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Please select a start date", Toast.LENGTH_LONG).show();
                    return;
                }
            default:
                break;
        }

        TaskType taskType = TaskType.Normal;
        switch (activityModel.getSelectedFilter()) {
            case Recurring:
                taskType = TaskType.Recurring;
                break;
            case Pending:
                taskType = TaskType.Pending;
                break;
            default:
                break;
        }

        if (activityModel.getSelectedFilter() != TaskFilterOption.Pending) {
            if (!view.oneTime.isChecked() && view.filterRadioGroup.getCheckedRadioButtonId() != -1) {
                taskType = TaskType.Recurring;
                name += ", ";

                if (view.daily.isChecked()) {
                    name += view.daily.getText();
                } else if (view.weekly.isChecked()) {
                    name += view.weekly.getText();
                    taskInterval = TaskInterval.Weekly;
                } else if (view.monthly.isChecked()) {
                    name += view.monthly.getText();
                    taskInterval = TaskInterval.Monthly;
                } else if (view.yearly.isChecked()) {
                    name += view.yearly.getText();
                    taskInterval = TaskInterval.Yearly;
                }
            } else {
                taskType = TaskType.Normal;
            }

            switch (activityModel.getSelectedFilter()) {
                case Tomorrow:
                    dueDate = dateManager.getDateFromFormattedString(dateManager.getTomorrowLongDate()) / (24 * 60 * 60 * 1000);
                    break;
                case Today:
                    dueDate = dateManager.getDateFromFormattedString(dateManager.getLongDate()) / (24 * 60 * 60 * 1000);
                    break;
                case Recurring:
                    dueDate = dateManager.getDateFromFormattedString(view.editTextDate.getText().toString()) / (24 * 60 * 60 * 1000);
                    break;
            }

            createDate = dueDate;
        }

        TaskContext context = TaskContext.Home;

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


        if (taskType == TaskType.Recurring) {
            var recurringTask = new SuccessoratorRecurringTask(null, name, -1, createDate, 0, taskInterval, context, -1, -1);
            activityModel.add(recurringTask);
        } else {
            var task = new SuccessoratorTask(null, name, -1, false, taskType, dueDate, context);
            activityModel.add(task);
        }

        dialog.dismiss();
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
