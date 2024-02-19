package edu.ucsd.cse110.successorator.ui.tasklist;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTasks;

public class SuccessoratorTaskListAdapter extends ArrayAdapter<SuccessoratorTask> {
    Consumer<SuccessoratorTask> onTaskClick;
    public SuccessoratorTaskListAdapter(
            Context context, List<SuccessoratorTask> tasks, Consumer<SuccessoratorTask> onTaskClick
    ) {
        super(context, 0, new ArrayList<>(tasks));
        this.onTaskClick = onTaskClick;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SuccessoratorTask task = getItem(position);
        assert task != null;


        ListItemTaskBinding binding;
        if (convertView != null) {
            binding = ListItemTaskBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemTaskBinding.inflate(layoutInflater, parent, false);
        }

        binding.getRoot().setOnClickListener(v -> {
            onTaskClick.accept(task);
        });
        
        binding.taskName.setText(task.getName());
        if (task.getIsComplete()) {
            binding.taskName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            binding.taskName.setPaintFlags(binding.taskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return binding.getRoot();
    }
}
