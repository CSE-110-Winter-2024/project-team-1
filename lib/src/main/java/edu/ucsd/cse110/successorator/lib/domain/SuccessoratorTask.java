package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class SuccessoratorTask {
    private final @NonNull String name;
    private final int sortOrder;
    private final @Nullable Integer id;

    private final @NonNull Boolean isComplete;

    private final @NonNull TaskType type;

    private final @NonNull long dueDate;

    private final @NonNull TaskContext context;

    public SuccessoratorTask(@Nullable Integer id, @NonNull String name, int sortOrder, @NonNull Boolean isComplete, @NonNull TaskType type, @NonNull long dueDate, TaskContext context) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.id = id;
        this.isComplete = isComplete;
        this.type = type;
        this.dueDate = dueDate;
        this.context = context;
    }

    public @Nullable Integer getId() {
        return id;
    }

    public @NonNull String getName() {
        return name;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public @NonNull Boolean getIsComplete() {
        return isComplete;
    }

    public @NonNull TaskType getType() {
        return type;
    }

    public @NonNull long getDueDate() {
        return dueDate;
    }

    public @NonNull TaskContext getContext() { return context; }

    public SuccessoratorTask withId(int id) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, dueDate, context);
    }

    public SuccessoratorTask withSortOrder(int sortOrder) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, dueDate, context);
    }

    public SuccessoratorTask withIsComplete(Boolean isComplete) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, dueDate, context);
    }

    public SuccessoratorTask withType(TaskType type) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, dueDate, context);
    }

    public SuccessoratorTask withDueDate(long dueDate) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, dueDate, context);
    }

    public SuccessoratorTask withContext(TaskContext context) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, dueDate, context);
    }
}