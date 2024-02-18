package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class SuccessoratorTask {
    private final @NonNull String name;
    private final int sortOrder;
    private final @Nullable Integer id;

    private final @NonNull Boolean isComplete;

    private final @NonNull long date;

    // no-date constructor
    public SuccessoratorTask(@Nullable Integer id, @NonNull String name, int sortOrder, @NonNull Boolean isComplete) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.id = id;
        this.isComplete = isComplete;
        this.date = System.currentTimeMillis();
    }

    // date constructor: not preferred, mainly used when importing old tasks (e.g. entity -> task conversion)
    public SuccessoratorTask(@Nullable Integer id, @NonNull String name, int sortOrder, @NonNull Boolean isComplete, @NonNull long date) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.id = id;
        this.isComplete = isComplete;
        this.date = date;
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

    public @NonNull long getDate() {
        return date;
    }

    public SuccessoratorTask withId(int id) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete);
    }

    public SuccessoratorTask withSortOrder(int sortOrder) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete);
    }

    public SuccessoratorTask withIsComplete(Boolean isComplete) {
        return new SuccessoratorTask(id, name, sortOrder, isComplete);
    }
}
