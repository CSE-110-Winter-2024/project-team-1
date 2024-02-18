package edu.ucsd.cse110.successorator.ui.tasklist;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
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

        var test = this.activityModel.getOrderedTasks();

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

        // Update dateText
        view.dateText.setText(dateManager.getDate());

        view.testDayChangeButton.setOnClickListener(v -> {
            activityModel.getOrderedTasks().removeObserver(dateObserver);
            var newDate = dateManager.incrementDate();
            view.dateText.setText(newDate);
            date.setValue(newDate);
        });

        return view.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        var test = dateManager.getDate();
        view.dateText.setText(dateManager.getDate());
        activityModel.getOrderedTasks().observe(dateObserver);
    }

}
