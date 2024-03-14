package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SuccessoratorTasksTest {
    @Test
    public void testToggleCompleteStart() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 0);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(2).getIsComplete().booleanValue());
        assertEquals("Task 1", modifiedTasks.get(2).getName());
    }

    @Test
    public void testToggleCompleteMiddle() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 1);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(2).getIsComplete().booleanValue());
        assertEquals("Task 2", modifiedTasks.get(2).getName());
    }

    @Test
    public void testToggleCompleteEnd() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 2);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(2).getIsComplete().booleanValue());
        assertEquals("Task 3", modifiedTasks.get(2).getName());
    }

    @Test
    public void testToggleCompleteTwice() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 1);
        modifiedTasks = SuccessoratorTasks.toggleComplete(modifiedTasks, 1);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(2).getIsComplete().booleanValue());
        assertEquals("Task 3", modifiedTasks.get(1).getName());
        assertEquals("Task 2", modifiedTasks.get(2).getName());
    }

    @Test
    public void testRemoveCompletedTasksOnce() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, true, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.removeCompletedTasks(tasks);

        assertEquals(2, modifiedTasks.size());
        assertEquals("Task 3", modifiedTasks.get(1).getName());

    }

    @Test
    public void testRemoveCompletedTasksTwice() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, true, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.removeCompletedTasks(tasks);

        assertEquals(2, modifiedTasks.size());
        assertEquals("Task 2", modifiedTasks.get(0).getName());

        var expected = modifiedTasks.toArray();
        modifiedTasks.add(new SuccessoratorTask(4, "Task 4", 3, true, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        modifiedTasks = SuccessoratorTasks.removeCompletedTasks(modifiedTasks);

        assertArrayEquals(expected, modifiedTasks.toArray());

    }

    @Test
    public void testUntoggle() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, true, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 2);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(2).getIsComplete().booleanValue());
        assertEquals("Task 3", modifiedTasks.get(0).getName());
        assertEquals("Task 1", modifiedTasks.get(1).getName());
        assertEquals("Task 2", modifiedTasks.get(2).getName());
    }

    @Test
    public void testDoubleToggle() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 1);
        modifiedTasks = SuccessoratorTasks.toggleComplete(modifiedTasks, 2);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(2).getIsComplete().booleanValue());
        assertEquals("Task 2", modifiedTasks.get(0).getName());
        assertEquals("Task 1", modifiedTasks.get(1).getName());
        assertEquals("Task 3", modifiedTasks.get(2).getName());
    }

    @Test
    public void testRescheduleOnce() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Recurring, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(1, "Task 2", 1, false, TaskType.Recurring, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Weekly, TaskContext.Home));
        tasks.add(new SuccessoratorTask(1, "Task 3", 2, false, TaskType.Recurring, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(1, "Task 4", 3, false, TaskType.Recurring, 0, 0, TaskInterval.Weekly, TaskContext.Home));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.rescheduleTasks(tasks);

        assertEquals("Task 1", tasks.get(0).getName());
        assertEquals(LocalDate.now().toEpochDay() + 1, modifiedTasks.get(0).getDueDate());
        assertEquals("Task 2", tasks.get(1).getName());
        assertEquals(LocalDate.now().toEpochDay() + 7, modifiedTasks.get(1).getDueDate());
        assertEquals("Task 3", tasks.get(2).getName());
        assertEquals(0, modifiedTasks.get(2).getDueDate());
        assertEquals("Task 4", tasks.get(3).getName());
        assertEquals(0, modifiedTasks.get(3).getDueDate());
    }

    @Test
    public void testRescheduleTwice() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Recurring, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(1, "Task 2", 1, false, TaskType.Recurring, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Weekly, TaskContext.Home));
        tasks.add(new SuccessoratorTask(1, "Task 3", 2, false, TaskType.Recurring, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(1, "Task 4", 3, false, TaskType.Recurring, 0, 0, TaskInterval.Weekly, TaskContext.Home));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.rescheduleTasks(tasks);

        assertEquals("Task 1", tasks.get(0).getName());
        assertEquals(LocalDate.now().toEpochDay() + 1, modifiedTasks.get(0).getDueDate());
        assertEquals("Task 2", tasks.get(1).getName());
        assertEquals(LocalDate.now().toEpochDay() + 7, modifiedTasks.get(1).getDueDate());
        assertEquals("Task 3", tasks.get(2).getName());
        assertEquals(0, modifiedTasks.get(2).getDueDate());
        assertEquals("Task 4", tasks.get(3).getName());
        assertEquals(0, modifiedTasks.get(3).getDueDate());

        modifiedTasks = SuccessoratorTasks.rescheduleTasks(modifiedTasks);

        assertEquals("Task 1", tasks.get(0).getName());
        assertEquals(LocalDate.now().toEpochDay() + 1, modifiedTasks.get(0).getDueDate());
        assertEquals("Task 2", tasks.get(1).getName());
        assertEquals(LocalDate.now().toEpochDay() + 7, modifiedTasks.get(1).getDueDate());
        assertEquals("Task 3", tasks.get(2).getName());
        assertEquals(0, modifiedTasks.get(2).getDueDate());
        assertEquals("Task 4", tasks.get(3).getName());
        assertEquals(0, modifiedTasks.get(3).getDueDate());
    }

    @Test
    public void testDeleteTask() {
        // Test deleting task at start
        List<SuccessoratorTask> tasks = createTasks();
        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.deleteTask(new ArrayList<>(tasks), 0);
        assertEquals(2, modifiedTasks.size());
        assertEquals("Task 2", modifiedTasks.get(0).getName());

        // Test deleting task at middle
        tasks = createTasks();
        modifiedTasks = SuccessoratorTasks.deleteTask(new ArrayList<>(tasks), 1);
        assertEquals(2, modifiedTasks.size());
        assertEquals("Task 1", modifiedTasks.get(0).getName());
        assertEquals("Task 3", modifiedTasks.get(1).getName());

        // Test deleting task at end
        tasks = createTasks();
        modifiedTasks = SuccessoratorTasks.deleteTask(new ArrayList<>(tasks), 2);
        assertEquals(2, modifiedTasks.size());
        assertEquals("Task 1", modifiedTasks.get(0).getName());
        assertEquals("Task 2", modifiedTasks.get(1).getName());
    }

    @Test
    public void testRescheduleTaskToToday() {
        // Test rescheduling task at start
        List<SuccessoratorTask> tasks = createTasks();
        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.rescheduleTaskToToday(new ArrayList<>(tasks), 0);
        assertEquals(LocalDate.now().toEpochDay(), modifiedTasks.get(0).getDueDate());

        // Test rescheduling pending task
        tasks = createTasks();
        modifiedTasks = SuccessoratorTasks.rescheduleTaskToToday(new ArrayList<>(tasks), 1);
        assertEquals(LocalDate.now().toEpochDay(), modifiedTasks.get(1).getDueDate());
        assertEquals(TaskType.Normal, modifiedTasks.get(1).getType());

        // Test rescheduling task at end
        tasks = createTasks();
        modifiedTasks = SuccessoratorTasks.rescheduleTaskToToday(new ArrayList<>(tasks), 2);
        assertEquals(LocalDate.now().toEpochDay(), modifiedTasks.get(2).getDueDate());
    }

    @Test
    public void testRescheduleTaskToTomorrow() {
        // Test rescheduling task at start
        List<SuccessoratorTask> tasks = createTasks();
        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.rescheduleTaskToTomorrow(new ArrayList<>(tasks), 0);
        assertEquals(LocalDate.now().plusDays(1).toEpochDay(), modifiedTasks.get(0).getDueDate());

        // Test rescheduling pending task
        tasks = createTasks();
        modifiedTasks = SuccessoratorTasks.rescheduleTaskToTomorrow(new ArrayList<>(tasks), 1);
        assertEquals(LocalDate.now().plusDays(1).toEpochDay(), modifiedTasks.get(1).getDueDate());
        assertEquals(TaskType.Normal, modifiedTasks.get(1).getType());

        // Test rescheduling task at end
        tasks = createTasks();
        modifiedTasks = SuccessoratorTasks.rescheduleTaskToTomorrow(new ArrayList<>(tasks), 2);
        assertEquals(LocalDate.now().plusDays(1).toEpochDay(), modifiedTasks.get(2).getDueDate());
    }

    private List<SuccessoratorTask> createTasks() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Pending, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        return tasks;
    }
}