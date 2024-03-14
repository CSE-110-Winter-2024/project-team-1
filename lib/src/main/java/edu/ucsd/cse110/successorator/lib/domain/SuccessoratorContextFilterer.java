package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class SuccessoratorContextFilterer {
    public static List<SuccessoratorTask> filterContext(TaskContext filter, List<SuccessoratorTask> tasks) {
        switch (filter) {
            case Home:
                return tasks.stream()
                        .filter(task -> task.getContext() == (TaskContext.Home))
                        .collect(java.util.stream.Collectors.toList());
            case Work:
                return tasks.stream()
                        .filter(task -> task.getContext() == (TaskContext.Work))
                        .collect(java.util.stream.Collectors.toList());
            case School:
                return tasks.stream()
                        .filter(task -> task.getContext() == (TaskContext.School))
                        .collect(java.util.stream.Collectors.toList());
            case Errands:
                return tasks.stream()
                        .filter(task -> task.getContext() == (TaskContext.Errands))
                        .collect(java.util.stream.Collectors.toList());
            case None:
                return tasks.stream().collect(java.util.stream.Collectors.toList());

            default:
                return tasks;
        }
    }
}
