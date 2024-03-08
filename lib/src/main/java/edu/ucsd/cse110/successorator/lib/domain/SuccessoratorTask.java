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

    private final @Nullable long createDate;

    private final @NonNull long dueDate;

    private final @Nullable TaskInterval interval;


    public SuccessoratorTask(@Nullable Integer id, @NonNull String name, int sortOrder, @NonNull Boolean isComplete, @NonNull TaskType type, @Nullable long createDate, @NonNull long dueDate, TaskInterval interval) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.id = id;
        this.isComplete = isComplete;
        this.type = type;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.interval = interval;
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

    public @Nullable long getCreateDate() {
        return createDate;
    }

    public @NonNull long getDueDate() {
        return dueDate;
    }

    public @Nullable TaskInterval getInterval() {
        return interval;
    }

    public SuccessoratorTask withId(int id) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, createDate, dueDate, interval);
    }

    public SuccessoratorTask withSortOrder(int sortOrder) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, createDate, dueDate, interval);
    }

    public SuccessoratorTask withIsComplete(Boolean isComplete) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, createDate, dueDate, interval);
    }

    public SuccessoratorTask withType(TaskType type) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, createDate, dueDate, interval);
    }

    public SuccessoratorTask withCreateDate(long createDate) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, createDate, dueDate, interval);
    }

    public SuccessoratorTask withDueDate(long dueDate) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, createDate, dueDate, interval);
    }

    public SuccessoratorTask withInterval(TaskInterval interval) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete, type, createDate, dueDate, interval);
    }
}