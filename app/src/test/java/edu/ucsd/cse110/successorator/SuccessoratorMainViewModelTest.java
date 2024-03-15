package edu.ucsd.cse110.successorator;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import androidx.lifecycle.LiveData;

import edu.ucsd.cse110.successorator.data.db.SuccessoratorTaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskFilterOption;
import edu.ucsd.cse110.successorator.lib.domain.TaskInterval;
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
import kotlinx.coroutines.scheduling.Task;

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

    @Test
    void filterTaskByCategory() {
        // GIVEN
        TaskType type = TaskType.Pending;
        model.add(new SuccessoratorTask(1, "Test1", 0, false, type, 1, TaskContext.Home));
        model.add(new SuccessoratorTask(2, "Test2", 0, false, TaskType.Normal, 1, TaskContext.Home));
        model.add(new SuccessoratorTask(3, "Test3", 0, false, TaskType.Recurring, 1, TaskContext.Home));
        model.add(new SuccessoratorTask(4, "Test4", 0, false, type, 1, TaskContext.Home));
        // WHEN
        model.changeFilter(TaskFilterOption.Pending);
        // THEN
        assertEquals(type, taskRepository.findAll().getValue().get(0).getType());
        assertEquals(type, taskRepository.findAll().getValue().get(1).getType());
    }
    @Test
    void createTaskWithContext() {
        // GIVEN
        TaskContext context = TaskContext.Errands;
        // WHEN
        model.add(new SuccessoratorTask(1, "Test", 1, false, TaskType.Normal, 0, context));
        // THEN
        List<SuccessoratorTask> tasks = model.getOrderedTasks().getValue();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(TaskContext.Errands, tasks.get(0).getContext());
    }

    @Test
    void filterTasksByContext() {
        // Given the user is on any task view
        TaskContext context = TaskContext.Work;
        model.add(new SuccessoratorTask(4, "Test", 0, false, TaskType.Pending, 1, TaskContext.School));
        model.add(new SuccessoratorTask(1, "Test2", 0, false, TaskType.Pending, 1, TaskContext.Errands));
        model.add(new SuccessoratorTask(2, "Test3", 0, false, TaskType.Pending, 1, TaskContext.Work));
        model.add(new SuccessoratorTask(3, "Test4", 0, false, TaskType.Pending, 1, TaskContext.Home));
        // When the user selects a context
        model.focus("Work");
        model.changeFilter(TaskFilterOption.Pending);
        // Then only the tasks with that context show up on task view
        assertEquals(context, taskRepository.findAll().getValue().get(0).getContext());
    }

    @Test
    void createRecurringTask() {
        // Given the user is on the view after pressing the plus button
        int createDate = 1;
        String name = "Recurring Task";
        TaskContext context = TaskContext.Home;

        // When the user selects an option for the task (daily, monthly, etc.)
        TaskInterval taskInterval =  TaskInterval.Daily;

        // And the user selects create
        var recurringTask = new SuccessoratorRecurringTask(null, name, -1, createDate, 0, taskInterval, context, -1, -1);
        model.add(recurringTask);

        // Then the task appears on the task view, and it keeps showing up whenever it occurs.
        assertEquals(name, recurringTaskRepository.findAll().getValue().get(0).getName());
    }

    @Test
    void moveAndEditTasks() {
        // Given the user is on the task view and there is a task present
        // When the user long presses on the task
        // Then a menu for moving, finishing, and deleting the task should appear.
    }

    @Test
    void seeCurrentTitleAtTopOfScreen() {
        // Given the user is on the task view
        // When the user selects the Today or Tomorrow views
        // Then the Today or Tomorrow text, along with the correct date, shows up as the title.
    }

    @Test
    void addNewTasksToCategory() {
        // Given the user is on the task view of any context
        // When the user clicks the plus button
        // Then the user is prompted with a view to input a task with a save button
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
