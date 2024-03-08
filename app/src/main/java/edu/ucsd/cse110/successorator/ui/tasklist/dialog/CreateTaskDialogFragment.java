package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

// android imports
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

// androidx imports
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

// internal imports
import java.time.LocalDate;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.TaskFilterOption;
import edu.ucsd.cse110.successorator.lib.domain.TaskInterval;
import edu.ucsd.cse110.successorator.lib.domain.TaskType;

public class CreateTaskDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private FragmentDialogCreateTaskBinding view;

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

        var dialog = new AlertDialog.Builder(getActivity())
                .setTitle("New Task")
                .setMessage("Please enter the new task's title.")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();

        view.taskNameEntry.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                return true;
            }
            return false;
        });

        return dialog;
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var name = view.taskNameEntry.getText().toString();

        // it doesn't really matter what we pass here, since the add method will extract
        // only the name from the task (consider changing? possible refactor could be helpful)
        long createDate = 0;
        long dueDate = 0;
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
                //TODO: add more cases for other filters
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

        TaskInterval taskInterval = TaskInterval.Daily;
        /*switch (activityModel.getSelectedFilter()) {
            case Daily:
                taskInterval = taskInterval.Daily;
            case Weekly:
                taskInterval = taskInterval.Weekly;
            case
        }*/

        var task = new SuccessoratorTask(null, name, -1, false, taskType, createDate, dueDate, taskInterval);

        activityModel.add(task);

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
