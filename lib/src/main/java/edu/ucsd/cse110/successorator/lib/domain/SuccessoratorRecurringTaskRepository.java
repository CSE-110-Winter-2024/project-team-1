package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface SuccessoratorRecurringTaskRepository {
    Subject<SuccessoratorRecurringTask> find(int id);

    Subject<List<SuccessoratorRecurringTask>> findAll();

    void add(SuccessoratorRecurringTask task);

    void save(SuccessoratorRecurringTask task);

    void save(List<SuccessoratorRecurringTask> tasks);

    void deleteAll();
}
