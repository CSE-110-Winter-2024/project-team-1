package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface SuccessoratorTaskRepository {
    Subject<SuccessoratorTask> find(int id);

    Subject<List<SuccessoratorTask>> findAll();

    void add(SuccessoratorTask task);

    void save(SuccessoratorTask task);

    void save(List<SuccessoratorTask> tasks);

    void markComplete(int id, int completedTasksBeginIndex);
}
