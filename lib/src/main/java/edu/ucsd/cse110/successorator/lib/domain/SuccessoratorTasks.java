package edu.ucsd.cse110.successorator.lib.domain;

import java.time.ZoneId;
import java.util.List;

// time libraries
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
        // we force this to midnight for now, consider passing as a parameter in the future
        // actual functionality pending further discussion
        long cutoff = OffsetDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, OffsetDateTime.now().getOffset()).toInstant().toEpochMilli();
        System.out.println(cutoff); // debug to verify
        return tasks.stream()
                .filter(task -> (!task.getIsComplete() || task.getDate() > cutoff))
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