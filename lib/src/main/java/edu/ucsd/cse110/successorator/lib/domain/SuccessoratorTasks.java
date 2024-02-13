package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

public class SuccessoratorTasks {
    public static List<SuccessoratorTask> insertTask(List<SuccessoratorTask> tasks, SuccessoratorTask task) {
        int i; // finished list start index
        for (i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getIsComplete()) {
                break;
            }
        }
        tasks.add(i, task);

        // update orders
        for (i = 0; i < tasks.size(); i++) {
            tasks.set(i, tasks.get(i).withSortOrder(i));
        }
        return tasks;
    }

    public static List<SuccessoratorTask> toggleComplete(List<SuccessoratorTask> tasks, int sortOrder) {
        var task = tasks.get(sortOrder);
        boolean modifiedState = !task.getIsComplete();
        var modifiedTask = task.withIsComplete(modifiedState);
        tasks.remove(sortOrder);
        return insertTask(tasks, modifiedTask);
    }

    public static List<SuccessoratorTask> removeCompletedTasks(List<SuccessoratorTask> tasks) {
        return tasks.stream()
                .filter(task -> !task.getIsComplete())
                .collect(java.util.stream.Collectors.toList());
    }
/*    public static List<SuccessoratorTask> toggleComplete(List<SuccessoratorTask> tasks, int sortOrder) {
        int i; // finished list start index
        for (i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getIsComplete()) {
                break;
            }
        }
        var task = tasks.get(sortOrder);
        boolean modifiedState = !task.getIsComplete();
        var modifiedTask = task.withIsComplete(modifiedState);
        if (modifiedState) {
            tasks.add(i, modifiedTask);
            tasks.remove(sortOrder);
        }
        else {
            // US-7
        }

        // update orders
        for (i = 0; i < tasks.size(); i++) {
            tasks.set(i, tasks.get(i).withSortOrder(i));
        }

        return tasks;
    }*/
}