package edu.ucsd.cse110.successorator.lib.domain;

import com.sun.net.httpserver.Authenticator;

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

    // since recurring tasks cant be marked as complete, this is largely unnecessary but we'll implement this way just in case, for future extensibility
    public static List<SuccessoratorRecurringTask> insertTask(List<SuccessoratorRecurringTask> tasks, SuccessoratorRecurringTask task, boolean atBoundary) {
        //int i = tasks.size() - 1; // start index
        /*if (atBoundary) { // insert task right after all finished tasks (desired for new/completed tasks)
            // iterate until first finished task is found
            for (i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getIsComplete()) {
                    break;
                }
            }
        }*/
        tasks.add(task);

        // update orders
        for (int i = 0; i < tasks.size(); i++) {
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

    public static int getId(List<SuccessoratorTask> tasks) {
        int id = 0;
        for (var task : tasks) {
            if (task.getId() > id) {
                id = task.getId();
            }
        }
        return id + 1;
    }

    public static List<SuccessoratorTask> scheduleTasks(List<SuccessoratorTask> tasks, SuccessoratorRecurringTask task) {
        System.out.println("Method called!");
        if (task.getCurrentTask() == -1 || task.getUpcomingTask() == -1) { // new task
            System.out.println("New task scheduled!");
            SuccessoratorTask currentTask = task.scheduleTask();
            SuccessoratorTask upcomingTask = task.scheduleTask();
            int id = getId(tasks);
            currentTask = currentTask.withId(id);
            upcomingTask = upcomingTask.withId(id + 1);
            task.setCurrentTask(id);
            task.setUpcomingTask(id + 1);

            System.out.println("Current task ID: " + currentTask.getId());
            System.out.println("Upcoming task ID: " + upcomingTask.getId());

            System.out.println("Current task date: " + currentTask.getDueDate());
            System.out.println("Upcoming task date: " + upcomingTask.getDueDate());

            tasks.add(currentTask);
            tasks.add(upcomingTask);

            System.out.println("Tasks added!");

            // update orders
            for (int i = 0; i < tasks.size(); i++) {
                tasks.set(i, tasks.get(i).withSortOrder(i));
            }

            return tasks;
        }
        // existing task
        // remove old task
        System.out.println("Tasks being updated!");
        int i;
        for (i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getCurrentTask()) {
                break;
            }
        }
        tasks.remove(i);
        System.out.println("Old task removed!");

        // shift new task to old task
        task.setCurrentTask(task.getUpcomingTask());

        // schedule a new task
        SuccessoratorTask upcomingTask = task.scheduleTask();
        int id = getId(tasks);
        upcomingTask = upcomingTask.withId(id);

        // update id
        task.setUpcomingTask(id);

        // add the task
        tasks.add(upcomingTask);
        System.out.println("New task added!");

        // update orders
        for (i = 0; i < tasks.size(); i++) {
            tasks.set(i, tasks.get(i).withSortOrder(i));
        }

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