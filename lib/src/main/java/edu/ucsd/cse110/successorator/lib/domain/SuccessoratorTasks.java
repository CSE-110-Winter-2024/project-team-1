package edu.ucsd.cse110.successorator.lib.domain;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;

public class SuccessoratorTasks {
    public static List<SuccessoratorTask> toggleComplete(List<SuccessoratorTask> tasks, int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IllegalArgumentException("Invalid sortOrder");
        }

        List<SuccessoratorTask> modifiedTasks = new ArrayList<>(tasks);
        SuccessoratorTask task = modifiedTasks.get(index);
        boolean modifiedState = !task.getIsComplete();
        modifiedTasks.set(index, task.withIsComplete(modifiedState));

        if (modifiedState) {
            int i;
            for (i = 0; i < modifiedTasks.size(); i++) {
                if (modifiedTasks.get(i).getIsComplete()) {
                    break;
                }
            }
            modifiedTasks.add(i, modifiedTasks.remove(index));
        } else {
            // Handle incomplete tasks
            // US-7 logic goes here
        }

        // Update sort orders
        for (int i = 0; i < modifiedTasks.size(); i++) {
            modifiedTasks.set(i, modifiedTasks.get(i).withSortOrder(i));
        }

        return modifiedTasks;
    }
}
