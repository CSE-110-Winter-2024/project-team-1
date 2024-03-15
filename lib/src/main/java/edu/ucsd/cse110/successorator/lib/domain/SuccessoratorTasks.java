package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class SuccessoratorTasks {
    static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

    public static List<SuccessoratorTask> insertByContext(List<SuccessoratorTask> tasks, SuccessoratorTask task, boolean prepend) {
        int boundary = 0;
        for (; boundary < tasks.size(); boundary++) {
            // find boundary between unfinished/finished
            if (tasks.get(boundary).getIsComplete()) {
                break;
            }
        }

        ArrayList<TaskContext> contexts = new ArrayList<>();

        contexts.add(TaskContext.Home);
        int hwBoundary = 0;
        for (; hwBoundary < boundary; hwBoundary++) {
            // find boundary between home/work
            if (!contexts.contains(tasks.get(hwBoundary).getContext())) {
                break;
            }
        }

        contexts.add(TaskContext.Work);
        int wsBoundary = 0;
        for (; wsBoundary < boundary; wsBoundary++) {
            // find boundary between work/school
            if (!contexts.contains(tasks.get(wsBoundary).getContext())) {
                break;
            }
        }

        contexts.add(TaskContext.School);
        int seBoundary = 0;
        for (; seBoundary < boundary; seBoundary++) {
            // find boundary between school/errands
            if (!contexts.contains(tasks.get(seBoundary).getContext())) {
                break;
            }
        }

        int i; // start index
        switch(task.getContext()) {
            // sort in the order of H, W, S, E
            case Errands:
                if (prepend) {
                    i = seBoundary;
                }
                else {
                    i = boundary;
                }
                break;
            case School:
                if (prepend) {
                    i = wsBoundary;
                }
                else {
                    i = seBoundary;
                }
                break;
            case Work:
                if (prepend) {
                    i = hwBoundary;
                }
                else {
                    i = wsBoundary;
                }
                break;
            default:
                if (prepend) {
                    i = 0;
                }
                else {
                    i = hwBoundary;
                }
                break;
        }
        if (task.getIsComplete()) {
            // only applies when toggling unfinished to finished
            i = boundary;
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
        var modifiedTask = task.withIsComplete(!task.getIsComplete());
        tasks.remove(sortOrder);
        return insertByContext(tasks, modifiedTask, true);
    }

    public static List<SuccessoratorTask> removeCompletedTasks(List<SuccessoratorTask> tasks, long date) {
        tasks = tasks.stream()
                .filter(task -> !task.getIsComplete())
                .collect(java.util.stream.Collectors.toList());
        for (int i = 0; i < tasks.size(); i++) {
            // rollover Today tasks
            if (tasks.get(i).getType() == TaskType.Normal && tasks.get(i).getDueDate() == date - 1) {
                tasks.set(i, tasks.get(i).withDueDate(tasks.get(i).getDueDate() + 1));
            }
        }
        return tasks;
    }

    public static List<SuccessoratorTask> deleteTask(List<SuccessoratorTask> tasks, int sortOrder) {
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