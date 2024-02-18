package edu.ucsd.cse110.successorator.ui.tasklist;

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
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTasks;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.successorator.util.DateManager;

public class SuccessoratorTaskListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTaskListBinding view;
    private SuccessoratorTaskListAdapter adapter;


    private DateManager dateManager = new DateManager();

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
            view.dateText.setText(dateManager.incrementDate());
            SuccessoratorTasks.removeCompletedTasks(activityModel.getOrderedTasks().getValue()); // DEBUG
        });

        return view.getRoot();
    }
}
