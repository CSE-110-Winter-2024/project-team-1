package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;

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

    public SuccessoratorTaskEntity(@NonNull String name, int sortOrder, Boolean isComplete) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.isComplete = isComplete;
    }

    public static SuccessoratorTaskEntity fromTask(@NonNull SuccessoratorTask task) {
        var newTask = new SuccessoratorTaskEntity(task.getName(), task.getSortOrder(), task.getIsComplete());
        newTask.id = task.getId();
        return newTask;
    }

    public @NonNull SuccessoratorTask toTask() {
        return new SuccessoratorTask(id, name, sortOrder, isComplete);
    }
}
