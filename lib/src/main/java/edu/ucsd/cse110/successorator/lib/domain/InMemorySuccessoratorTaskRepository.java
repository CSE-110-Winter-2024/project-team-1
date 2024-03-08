package edu.ucsd.cse110.successorator.lib.domain;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class InMemorySuccessoratorTaskRepository implements SuccessoratorTaskRepository {
    private final List<SuccessoratorTask> tasks;
    private final Subject<List<SuccessoratorTask>> tasksSubject;
    private int nextId;

    public InMemorySuccessoratorTaskRepository() {
        tasks = new ArrayList<>();
        tasksSubject = new SimpleSubject<>();
        nextId = 1;
    }

    @Override
    public Subject<SuccessoratorTask> find(int id) {
        for (SuccessoratorTask task : tasks) {
            if (task.getId() == id) {
                return new SimpleSubject<>();
            }
        }
        return null;
    }

    @Override
    public Subject<List<SuccessoratorTask>> findAll() {
        return tasksSubject;
    }

    @Override
    public void add(SuccessoratorTask task) {
        task = new SuccessoratorTask(nextId++, task.getName(), task.getSortOrder(), task.getIsComplete(), task.getType(), task.getCreateDate(), task.getDueDate(), task.getInterval());
        tasks.add(task);
    }

    @Override
    public void save(SuccessoratorTask task) {
        return;
    }

    @Override
    public void save(List<SuccessoratorTask> tasks) {
        return;
    }

    @Override
    public void markComplete(int id, int completedTasksBeginIndex) {
        return;
    }

    @Override
    public void deleteAll() {
        tasks.clear();
    }
}