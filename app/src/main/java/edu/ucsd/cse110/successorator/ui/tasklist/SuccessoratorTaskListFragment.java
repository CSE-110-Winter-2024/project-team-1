package edu.ucsd.cse110.successorator.ui.tasklist;

import android.content.Intent;
import android.content.IntentFilter;
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
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.successorator.util.DateChangeReceiver;
import edu.ucsd.cse110.successorator.util.DateManager;

public class SuccessoratorTaskListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTaskListBinding view;
    private SuccessoratorTaskListAdapter adapter;

    private DateChangeReceiver dateChangeReceiver;

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

        dateChangeReceiver = new DateChangeReceiver(this::updateDate);

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
        });

        return view.getRoot();
    }

    private void updateDate() {
        // Get current date
        String formattedDate = dateManager.getDate();

        // Update dateText
        view.dateText.setText(formattedDate);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register BroadcastReceiver to listen for date changes
        IntentFilter filter = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        requireContext().registerReceiver(dateChangeReceiver, filter);

        // Update date text
        updateDate();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister BroadcastReceiver
        requireContext().unregisterReceiver(dateChangeReceiver);
    }
}
