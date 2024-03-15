package edu.ucsd.cse110.successorator.lib.domain;

import com.sun.net.httpserver.Authenticator;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class SuccessoratorTasks {
    static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

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

    // since recurring tasks cant be marked as complete, this is largely unnecessary but we'll implement this way just in case, for future extensibility
    public static List<SuccessoratorRecurringTask> insertTask(List<SuccessoratorRecurringTask> tasks, SuccessoratorRecurringTask task, boolean atBoundary) {
        int i = 0; // start index
        /*if (atBoundary) { // insert task right after all finished tasks (desired for new/completed tasks)
            // iterate until first finished task is found
            for (i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getIsComplete()) {
                    break;
                }
            }
        }*/
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

    public static List<SuccessoratorTask> deleteTask(List<SuccessoratorTask> tasks, int sortOrder) {
        tasks.remove(sortOrder);
        for (int i = 0; i < tasks.size(); i++) {
            tasks.set(i, tasks.get(i).withSortOrder(i));
        }
        return tasks;
    }

    public static List<SuccessoratorRecurringTask> deleteRecurringTask(List<SuccessoratorRecurringTask> tasks, int sortOrder) {
        tasks.remove(sortOrder);
        for (int i = 0; i < tasks.size(); i++) {
            tasks.set(i, tasks.get(i).withSortOrder(i));
        }
        return tasks;
    }

    public static List<SuccessoratorTask> rescheduleTaskToToday(List<SuccessoratorTask> tasks, int sortOrder) {
        var task = tasks.get(sortOrder);
        var modifiedTask = task.withDueDate(LocalDate.now().toEpochDay());
        if (task.getType() == TaskType.Pending) {
            modifiedTask = modifiedTask.withType(TaskType.Normal);
        }
        tasks.set(sortOrder, modifiedTask);
        return tasks;
    }

    public static List<SuccessoratorTask> rescheduleTaskToTomorrow(List<SuccessoratorTask> tasks, int sortOrder) {
        var task = tasks.get(sortOrder);
        var modifiedTask = task.withDueDate(LocalDate.now().plusDays(1).toEpochDay());
        if (task.getType() == TaskType.Pending) {
            modifiedTask = modifiedTask.withType(TaskType.Normal);
        }
        tasks.set(sortOrder, modifiedTask);
        return tasks;
    }

    public static List<SuccessoratorTask> rescheduleTasks(List<SuccessoratorTask> tasks) {
        return tasks.stream()
                .map(task -> rescheduleGuard(task))
                .collect(java.util.stream.Collectors.toList());
    }

    // guarded function to keep map clean
    public static SuccessoratorTask rescheduleGuard(SuccessoratorTask task) {
        if (task.getType() == TaskType.Recurring && task.getDueDate() == LocalDate.now().toEpochDay()) {
            //return rescheduleTask(task);
            return null;
        }
        return task;
    }
}