package edu.ucsd.cse110.successorator.ui.tasklist;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.TaskContextMenuOption;
import edu.ucsd.cse110.successorator.lib.domain.TaskFilterOption;
import edu.ucsd.cse110.successorator.lib.domain.TaskType;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.successorator.util.DateManager;

public class SuccessoratorTaskListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTaskListBinding view;
    private SuccessoratorTaskListAdapter adapter;


    private DateManager dateManager = new DateManager();

    private MutableSubject<String> date;

    private SharedPreferences sharedPreferences;

    /* makes sure date is changed after orderedTasks are created */
    private Observer<List<SuccessoratorTask>> dateObserver = tasks -> {
        if (tasks != null) {
            var currDate = dateManager.getDate();
            var prevDate = sharedPreferences.getString("prevDate", "");
            sharedPreferences.edit().putString("prevDate", currDate).apply();
            if (!prevDate.equals("") && !prevDate.equals(currDate)) {
                date.setValue(currDate);
            }
        }
    };

    public SuccessoratorTaskListFragment() {
    }

    public static SuccessoratorTaskListFragment newInstance() {
        SuccessoratorTaskListFragment fragment = new SuccessoratorTaskListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        date = new SimpleSubject<>();

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        this.adapter = new SuccessoratorTaskListAdapter(
                requireContext(),
                List.of(),
                task -> {
                    activityModel.markComplete(task.getSortOrder());
                }
        );

        activityModel.getOrderedTasks().observe(tasks -> {
            if (tasks != null) {
                adapter.clear();
                adapter.addAll(new ArrayList<>(tasks));
                adapter.notifyDataSetChanged();
            }
        });

        date.observe(date -> {
            if (date != null) {
                android.util.Log.d("date", "date modified " + date);
                activityModel.removeFinishedTasks();
                activityModel.rescheduleTasks();
            }
        });

        sharedPreferences = getActivity().getSharedPreferences("successorator", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            sharedPreferences.edit().putString("prevDate", dateManager.getDate()).apply();
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply();
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        this.view = FragmentTaskListBinding.inflate(inflater, container, false);

        this.view.taskList.setAdapter(adapter);
        this.view.taskList.setEmptyView(this.view.emptyText);
        // link button with creation fragment
        view.addTaskButton.setOnClickListener(v -> {
            var dialogFragment = CreateTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
        });

        final List<Object> dropdownItems = new ArrayList<>(Arrays.asList(TaskFilterOption.values()));
        dropdownItems.set(0, TaskFilterOption.values()[0] + dateManager.getDate());
        dropdownItems.set(1, TaskFilterOption.values()[1] + dateManager.getTomorrow());

        var spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                dropdownItems
        ) {
            // override to center text
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                return view;
            }
        };
        view.filterSpinner.setAdapter(spinnerAdapter);

        date.observe(date -> {
            if (date != null) {
                dropdownItems.set(0, TaskFilterOption.values()[0] + dateManager.getDate());
                dropdownItems.set(1, TaskFilterOption.values()[1] + dateManager.getTomorrow());
                spinnerAdapter.notifyDataSetChanged();
            }
        });

        view.filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TaskFilterOption selectedOption = TaskFilterOption.values()[position];
                activityModel.changeFilter(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
            }
        });
        registerForContextMenu(view.taskList);
        return view.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        dateManager = new DateManager();
        if (activityModel.getOrderedTasks().getValue() == null) {
            // make sure date is changed after orderedTasks are created
            activityModel.getOrderedTasks().observe(dateObserver);
        }
        else {
            // also update date on resume
            var currDate = dateManager.getDate();
            var prevDate = sharedPreferences.getString("prevDate", "");
            sharedPreferences.edit().putString("prevDate", currDate).apply();
            if (!prevDate.equals("") && !prevDate.equals(currDate)) {
                date.setValue(dateManager.getDate());
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (activityModel.getSelectedFilter() == TaskFilterOption.Pending || activityModel.getSelectedFilter() == TaskFilterOption.Recurring) {
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle("Task Options");
            if (activityModel.getSelectedFilter() == TaskFilterOption.Pending) {
                menu.add(0, v.getId(), 0, TaskContextMenuOption.MoveToToday.getTitle());
                menu.add(0, v.getId(), 0, TaskContextMenuOption.MoveToTomorrow.getTitle());
                menu.add(0, v.getId(), 0, TaskContextMenuOption.Finish.getTitle());
            }
            menu.add(0, v.getId(), 0, TaskContextMenuOption.Delete.getTitle());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        SuccessoratorTask task = adapter.getItem(position);

        assert task != null;

        if (item.getTitle() == TaskContextMenuOption.MoveToToday.getTitle()) {
            // Implement move to today action
            activityModel.rescheduleTaskToToday(task.getSortOrder());
        } else if (item.getTitle() == TaskContextMenuOption.MoveToTomorrow.getTitle()) {
            // Implement move to tomorrow action
            activityModel.rescheduleTaskToTomorrow(task.getSortOrder());
        } else if (item.getTitle() == TaskContextMenuOption.Finish.getTitle()) {
            if (task.getType() == TaskType.Pending) {
                activityModel.rescheduleTaskToToday(task.getSortOrder());
            }
            // Implement finish task action
            activityModel.markComplete(task.getSortOrder());
        } else if (item.getTitle() == TaskContextMenuOption.Delete.getTitle()) {
            // Implement delete task action
            activityModel.removeTask(task.getSortOrder());
        } else {
            return false;
        }
        return true;
    }
}
