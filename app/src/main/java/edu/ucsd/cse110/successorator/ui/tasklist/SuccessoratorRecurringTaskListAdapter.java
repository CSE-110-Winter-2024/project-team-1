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
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.TaskType;

public class SuccessoratorRecurringTaskListAdapter extends ArrayAdapter<SuccessoratorRecurringTask> {
    Consumer<SuccessoratorTask> onTaskClick;
    public SuccessoratorRecurringTaskListAdapter(
            Context context, List<SuccessoratorRecurringTask> tasks) {
        super(context, 0, new ArrayList<>(tasks));
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SuccessoratorRecurringTask task = getItem(position);
        assert task != null;


        ListItemTaskBinding binding;
        if (convertView != null) {
            binding = ListItemTaskBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemTaskBinding.inflate(layoutInflater, parent, false);
        }

        binding.getRoot().setOnLongClickListener(v -> {
            parent.showContextMenuForChild(v);
            return true;
        });

        binding.taskName.setText(task.getName());
        binding.taskName.setPaintFlags(binding.taskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        int drawableResourceId;
        switch (task.getContext()) {
            case Work:
                drawableResourceId = R.drawable.work;
                break;
            case School:
                drawableResourceId = R.drawable.school;
                break;
            case Errands:
                drawableResourceId = R.drawable.errands;
                break;
            default:
                drawableResourceId = R.drawable.home;
                break;
        }
        binding.context.setImageResource(drawableResourceId);

        return binding.getRoot();
    }
}
