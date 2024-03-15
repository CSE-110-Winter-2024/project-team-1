package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.TaskInterval;
import edu.ucsd.cse110.successorator.lib.domain.TaskType;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;

@Entity(tableName = "successoratorRecurringTasks")
public class SuccessoratorRecurringTaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "create_date")
    public long createDate;

    @ColumnInfo(name = "schedule_count")
    public int scheduleCount;

    @ColumnInfo(name = "interval")
    public String interval = TaskInterval.Daily.name();

    @ColumnInfo(name = "context")
    public String context = TaskContext.Home.name();

    @ColumnInfo(name = "current_task")
    public long currentTask;

    @ColumnInfo(name = "upcoming_task")
    public long upcomingTask;

    public SuccessoratorRecurringTaskEntity(@NonNull String name, int sortOrder, long createDate, int scheduleCount, String interval, String context, long currentTask, long upcomingTask) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.createDate = createDate;
        this.scheduleCount = scheduleCount;
        this.interval = interval;
        this.context = context;
        this.currentTask = currentTask;
        this.upcomingTask = upcomingTask;

    }

    public static SuccessoratorRecurringTaskEntity fromTask(@NonNull SuccessoratorRecurringTask task) {
        var newTask = new SuccessoratorRecurringTaskEntity(task.getName(), task.getSortOrder(), task.getCreateDate(), task.getScheduleCount(), task.getInterval().name(), task.getContext().name(), task.getCurrentTask(), task.getUpcomingTask());
        newTask.id = task.getId(); // is this line necessary?
        return newTask;
    }

    public @NonNull SuccessoratorRecurringTask toTask() {
        return new SuccessoratorRecurringTask(id, name, sortOrder, createDate, scheduleCount, TaskInterval.valueOf(interval), TaskContext.valueOf(context), currentTask, upcomingTask);
    }
}
