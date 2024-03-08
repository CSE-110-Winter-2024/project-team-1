package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

public class SuccessoratorTasks {
    public static List<SuccessoratorTask> insertTask(List<SuccessoratorTask> tasks, SuccessoratorTask task, boolean atBoundary) {
        int i = 0; // start index
        if (atBoundary) { // insert task right after all finished tasks (desired for new/completed tasks)
            // iterate until first finished task is found
            for (i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getIsComplete()) {
                    break;
                }
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
        boolean isComplete = !task.getIsComplete(); // simple toggle
        var modifiedTask = task.withIsComplete(isComplete);
        tasks.remove(sortOrder);
        if (isComplete) { // task was completed
            return insertTask(tasks, modifiedTask, true); // insert the completed task using custom func.
        }
        return insertTask(tasks, modifiedTask, false);
    }

    public static List<SuccessoratorTask> removeCompletedTasks(List<SuccessoratorTask> tasks) {
        return tasks.stream()
                .filter(task -> !task.getIsComplete())
                .collect(java.util.stream.Collectors.toList());
    }

    public static SuccessoratorTask rescheduleTask(SuccessoratorTask task) {
        long newDate = task.getDueDate();
        switch(task.getInterval()) { // naively add times
            // TODO: handle this properly.
            case Daily:
                newDate += 86400 * 1000;
                break;
            case Weekly:
                newDate += 86400 * 1000 * 7;
                break;
            case Monthly:
                newDate += 86400 * 1000 * 31;
                break;
            case Yearly:
                newDate += 86400 * 1000 * 365;
                break;
        }
        return task.withDueDate(newDate);
    }
}