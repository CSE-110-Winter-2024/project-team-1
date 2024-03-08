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

    public static List<SuccessoratorTask> rescheduleTasks(List<SuccessoratorTask> tasks) {
        return tasks.stream()
                .map(task -> rescheduleGuard(task))
                .collect(java.util.stream.Collectors.toList());
    }

    // guarded function to keep map clean
    public static SuccessoratorTask rescheduleGuard(SuccessoratorTask task) {
        if (task.getType() == TaskType.Recurring && task.getDueDate() == LocalDate.now().toEpochDay()) {
            return rescheduleTask(task);
        }
        return task;
    }

    public static SuccessoratorTask rescheduleTask(SuccessoratorTask task) {
        Calendar newDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Calendar originalDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        newDate.setTimeInMillis(task.getDueDate() * MILLISECONDS_IN_DAY); // necessary because no setTimeInDays method exists
        switch(task.getInterval()) {
            case Daily:
                newDate.add(Calendar.DATE, 1);
                break;
            case Weekly:
                newDate.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case Monthly:
                // first, get the intended day (e.g. 3rd saturday of month)
                // defer computation of original date for (probably marginal) performance gains
                originalDate.setTimeInMillis(task.getCreateDate() * MILLISECONDS_IN_DAY);
                int dayOfWeek = originalDate.get(Calendar.DAY_OF_WEEK);
                int weekOfMonth = originalDate.get(Calendar.DAY_OF_WEEK_IN_MONTH);

                // then, increment our date by a month
                // we only increment if the actual week matches the expected week
                // if it doesn't, then the previous month must have overflowed
                // this means the month already incremented, so we avoid double incrementing
                if (newDate.get(Calendar.DAY_OF_WEEK_IN_MONTH) == weekOfMonth) {
                    newDate.add(Calendar.MONTH, 1);
                }

                // next, set according day. calendar will overflow for us if necessary
                newDate.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                newDate.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekOfMonth);
                break;
            case Yearly:
                originalDate.setTimeInMillis(task.getCreateDate() * MILLISECONDS_IN_DAY);

                newDate.add(Calendar.YEAR, 1);

                int dayOfMonth = originalDate.get(Calendar.DAY_OF_MONTH);
                int month = originalDate.get(Calendar.MONTH);

                newDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newDate.set(Calendar.MONTH, month);
                break;
        }
        return task.withDueDate(newDate.getTimeInMillis()/MILLISECONDS_IN_DAY);
    }
}