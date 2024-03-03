package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDate;
import java.util.List;

public class SuccessoratorTasksFilterer {
    public static List<SuccessoratorTask> filterTasks(TaskFilterOption filter, List<SuccessoratorTask> tasks) {
        switch (filter) {
            case Today:
                // find all tasks that are due today and return them
                return tasks.stream()
                        .filter(task -> task.getDueDate() == (LocalDate.now().toEpochDay()))
                        .collect(java.util.stream.Collectors.toList());
            case Tomorrow:
                return tasks.stream()
                        .filter(task -> task.getDueDate() == (LocalDate.now().plusDays(1).toEpochDay()))
                        .collect(java.util.stream.Collectors.toList());
            case Recurring:
                return tasks.stream()
                        .filter(task -> task.getType() == TaskType.Recurring)
                        .collect(java.util.stream.Collectors.toList());
            case Pending:
                return tasks.stream()
                        .filter(task -> task.getType() == TaskType.Pending)
                        .collect(java.util.stream.Collectors.toList());
            default:
                return tasks;
        }
    }
}
