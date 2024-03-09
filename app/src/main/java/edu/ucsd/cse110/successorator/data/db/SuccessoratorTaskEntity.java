package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.TaskInterval;
import edu.ucsd.cse110.successorator.lib.domain.TaskType;

@Entity(tableName = "successoratorTasks")
public class SuccessoratorTaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "is_complete")
    public Boolean isComplete = false;

    @ColumnInfo(name = "type")
    public String type = TaskType.Normal.name();

    @ColumnInfo(name = "create_date")
    public long createDate;

    @ColumnInfo(name = "due_date")
    public long dueDate;

    @ColumnInfo(name = "interval")
    public String interval = TaskInterval.Daily.name();

    public SuccessoratorTaskEntity(@NonNull String name, int sortOrder, Boolean isComplete, String type, long createDate, long dueDate, String interval) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.isComplete = isComplete;
        this.type = type;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.interval = interval;
    }

    public static SuccessoratorTaskEntity fromTask(@NonNull SuccessoratorTask task) {
        var newTask = new SuccessoratorTaskEntity(task.getName(), task.getSortOrder(), task.getIsComplete(), task.getType().name(), task.getCreateDate(), task.getDueDate(), task.getInterval().name());
        newTask.id = task.getId();
        return newTask;
    }

    public @NonNull SuccessoratorTask toTask() {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, TaskType.valueOf(type), createDate, dueDate, TaskInterval.valueOf(interval));
    }
}
