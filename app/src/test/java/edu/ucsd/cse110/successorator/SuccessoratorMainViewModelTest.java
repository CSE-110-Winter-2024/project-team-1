package edu.ucsd.cse110.successorator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.lifecycle.LiveData;

import edu.ucsd.cse110.successorator.data.db.SuccessoratorTaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.data.db.RoomSuccessoratorTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorTaskDao;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorTaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskType;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class SuccessoratorMainViewModelTest {
    private MainViewModel model;
    private SuccessoratorTaskRepository taskRepository;
    private SuccessoratorRecurringTaskRepository recurringTaskRepository;


    @Before
    public void setUp() {
        taskRepository = new MockSuccessoratorTaskRepository();
        recurringTaskRepository = new MockSuccessoratorRecurringTaskRepository();
        model = new MainViewModel(taskRepository, recurringTaskRepository);
    }

    @Test
    public void savedTaskSuccessfully() {
        // GIVEN
        String taskText = "Sample Task";

        // WHEN
        model.add(new SuccessoratorTask(1, taskText, 0, false, TaskType.Normal, 1, TaskContext.Home));

        // THEN
        assertEquals(taskText, taskRepository.findAll().getValue().get(0).getName());
    }

    private static class MockSuccessoratorTaskRepository implements SuccessoratorTaskRepository {

        private final Map<Integer, SuccessoratorTask> tasks = new HashMap<>();

        @Override
        public Subject<SuccessoratorTask> find(int id) {
            return new SimpleSubject(tasks.get(id));
        }

        @Override
        public Subject<List<SuccessoratorTask>> findAll() {
            return new SimpleSubject(new ArrayList<>(tasks.values()));
        }

        @Override
        public void add(SuccessoratorTask task) {
            tasks.put(task.getId(), task);
        }

        @Override
        public void save(SuccessoratorTask task) {
            tasks.put(task.getId(), task);
        }

        @Override
        public void save(List<SuccessoratorTask> tasks) {
            for (SuccessoratorTask task : tasks) {
                this.tasks.put(task.getId(), task);
            }
        }

        @Override
        public void markComplete(int id, int completedTasksBeginIndex) {
            SuccessoratorTask task = tasks.get(id);
            if (task != null) {
                task.withIsComplete(true);
            }
        }

        @Override
        public void deleteAll() {
            tasks.clear();
        }

        // Additional method for testing purposes
        public Map<Integer, SuccessoratorTask> getTasks() {
            return tasks;
        }
    }
    private static class MockSuccessoratorRecurringTaskRepository implements SuccessoratorRecurringTaskRepository {

        private final Map<Integer, SuccessoratorRecurringTask> tasks = new HashMap<>();

        @Override
        public Subject<SuccessoratorRecurringTask> find(int id) {
            return new SimpleSubject<>(tasks.get(id));
        }

        @Override
        public Subject<List<SuccessoratorRecurringTask>> findAll() {
            return new SimpleSubject<>(new ArrayList<>(tasks.values()));
        }

        @Override
        public void add(SuccessoratorRecurringTask task) {
            tasks.put(task.getId(), task);
        }

        @Override
        public void save(SuccessoratorRecurringTask task) {
            tasks.put(task.getId(), task);
        }

        @Override
        public void save(List<SuccessoratorRecurringTask> tasks) {
            for (SuccessoratorRecurringTask task : tasks) {
                this.tasks.put(task.getId(), task);
            }
        }

        @Override
        public void deleteAll() {
            tasks.clear();
        }
    }
}
