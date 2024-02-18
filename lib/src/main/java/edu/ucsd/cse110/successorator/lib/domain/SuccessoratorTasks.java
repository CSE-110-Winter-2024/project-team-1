package edu.ucsd.cse110.successorator.lib.domain;

import java.time.ZoneId;
import java.util.List;

// time libraries
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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