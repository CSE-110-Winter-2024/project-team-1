package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.TimeZone;

public class SuccessoratorRecurringTask {
    private final @NonNull String name;
    private final int sortOrder;
    private final @Nullable Integer id;
    private final @NonNull long createDate;
    private @NonNull int scheduleCount;
    private final @NonNull TaskInterval interval;
    private final @NonNull TaskContext context;
    private @Nullable long currentTask;
    private @Nullable long upcomingTask;

    static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

    public SuccessoratorRecurringTask(@Nullable Integer id, @NonNull String name, int sortOrder, @Nullable long createDate, @NonNull int scheduleCount, TaskInterval interval, TaskContext context, @Nullable long currentTask, @Nullable long upcomingTask) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.id = id;
        this.createDate = createDate;
        this.scheduleCount = scheduleCount;
        this.interval = interval;
        this.context = context;
        this.currentTask = upcomingTask;
        this.upcomingTask = currentTask;
    }

    public @NonNull String getName() {
        return name;
    }

    public @Nullable Integer getId() {
        return id;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public @Nullable long getCreateDate() {
        return createDate;
    }

    public @NonNull int getScheduleCount() {
        return scheduleCount;
    }

    public @Nullable TaskInterval getInterval() {
        return interval;
    }
    public @NonNull TaskContext getContext() { return context; }

    public @Nullable long getCurrentTask() {
        return currentTask;
    }

    public @Nullable long getUpcomingTask() {
        return upcomingTask;
    }

    public SuccessoratorRecurringTask withId(int id) {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, interval, context, currentTask, upcomingTask);
    }

    public SuccessoratorRecurringTask withSortOrder(int sortOrder) {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, interval, context, currentTask, upcomingTask);
    }

    public SuccessoratorRecurringTask withIsComplete(Boolean isComplete) {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, interval, context, currentTask, upcomingTask);
    }

    public SuccessoratorRecurringTask withType(TaskType type) {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, interval, context, currentTask, upcomingTask);
    }

    public SuccessoratorRecurringTask withCreateDate(long createDate) {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, interval, context, currentTask, upcomingTask);
    }

    public SuccessoratorRecurringTask withScheduleCount(int scheduleCount) {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, interval, context, currentTask, upcomingTask);
    }

    public SuccessoratorRecurringTask withInterval(TaskInterval interval) {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, interval, context, currentTask, upcomingTask);
    }

    public SuccessoratorRecurringTask withContext(TaskContext context) {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, interval, context, currentTask, upcomingTask);
    }

    public SuccessoratorRecurringTask withCurrentTask(long currentTask) {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, interval, context, currentTask, upcomingTask);
    }

    public SuccessoratorRecurringTask withUpcomingTask(long upcomingTask) {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, interval, context, currentTask, upcomingTask);
    }

    public void setCurrentTask(long currentTask) {
        this.currentTask = currentTask;
    }

    public void setUpcomingTask(long upcomingTask) {
        this.upcomingTask = upcomingTask;
    }

    public SuccessoratorTask scheduleTask() {
        scheduleCount++;

        Calendar newDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Calendar originalDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        newDate.setTimeInMillis(getCreateDate() * MILLISECONDS_IN_DAY); // necessary because no setTimeInDays method exists
        switch(interval) {
            case Daily:
                newDate.add(Calendar.DATE, scheduleCount);
                break;
            case Weekly:
                newDate.add(Calendar.WEEK_OF_YEAR, scheduleCount);
                break;
            case Monthly:
                // first, get the intended day (e.g. 3rd saturday of month)
                // defer computation of original date for (probably marginal) performance gains
                originalDate.setTimeInMillis(getCreateDate() * MILLISECONDS_IN_DAY);
                newDate.add(Calendar.MONTH, scheduleCount);
                int dayOfWeek = originalDate.get(Calendar.DAY_OF_WEEK);
                int weekOfMonth = originalDate.get(Calendar.DAY_OF_WEEK_IN_MONTH);

                // then, increment our date by a month
                // we only increment if the actual week matches the expected week
                // if it doesn't, then the previous month must have overflowed
                // this means the month already incremented, so we avoid double incrementing
                for (int i = 0; i < scheduleCount; i++) {
                    if (newDate.get(Calendar.DAY_OF_WEEK_IN_MONTH) == weekOfMonth) {
                        newDate.add(Calendar.MONTH, 1);
                    }

                    // next, set according day. calendar will overflow for us if necessary
                    newDate.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                    newDate.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekOfMonth);
                }
                break;
            case Yearly:
                originalDate.setTimeInMillis(getCreateDate() * MILLISECONDS_IN_DAY);
                newDate.add(Calendar.YEAR, scheduleCount);

                int dayOfMonth = originalDate.get(Calendar.DAY_OF_MONTH);
                int month = originalDate.get(Calendar.MONTH);

                newDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newDate.set(Calendar.MONTH, month);
                break;
        }
        return new SuccessoratorTask(null, name, 1, false, TaskType.Recurring, 0, newDate.getTimeInMillis()/MILLISECONDS_IN_DAY, TaskInterval.Daily, context);
    }
}