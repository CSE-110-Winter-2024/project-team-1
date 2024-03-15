package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SuccessoratorTasksFiltererTest {
    @Test
    public void testFilterToday() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, LocalDate.now().toEpochDay(), TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, TaskContext.Home));

        List<SuccessoratorTask> filteredTasks = SuccessoratorTasksFilterer.filterTasks(TaskFilterOption.Today, tasks);

        assertEquals(1, filteredTasks.size());
    }

    @Test
    public void testFilterTomorrow() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, LocalDate.now().plusDays(1).toEpochDay(), TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, LocalDate.now().toEpochDay(), TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, TaskContext.Home));

        List<SuccessoratorTask> filteredTasks = SuccessoratorTasksFilterer.filterTasks(TaskFilterOption.Tomorrow, tasks);

        assertEquals(1, filteredTasks.size());
    }

    @Test
    public void testFilterRecurring() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Recurring, 0, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Pending, 0, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, TaskContext.Home));

        List<SuccessoratorTask> filteredTasks = SuccessoratorTasksFilterer.filterTasks(TaskFilterOption.Recurring, tasks);

        assertEquals(1, filteredTasks.size());
    }

    @Test
    public void testFilterPending() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Pending, 0, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, TaskContext.Home));

        List<SuccessoratorTask> filteredTasks = SuccessoratorTasksFilterer.filterTasks(TaskFilterOption.Pending, tasks);

        assertEquals(1, filteredTasks.size());
    }

    @Test
    public void testFilterEmpty() {
        List<SuccessoratorTask> tasks = new ArrayList<>();

        List<SuccessoratorTask> filteredTasks = SuccessoratorTasksFilterer.filterTasks(TaskFilterOption.Today, tasks);

        assertEquals(0, filteredTasks.size());
    }

    @Test
    public void testFilterByPendingWithNoPendingTasks() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, TaskContext.Home));

        List<SuccessoratorTask> filteredTasks = SuccessoratorTasksFilterer.filterTasks(TaskFilterOption.Pending, tasks);

        assertEquals(0, filteredTasks.size());
    }

    @Test
    public void testFilterByRecurringWithNoRecurringTasks() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, TaskContext.Home));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, TaskContext.Home));

        List<SuccessoratorTask> filteredTasks = SuccessoratorTasksFilterer.filterTasks(TaskFilterOption.Recurring, tasks);

        assertEquals(0, filteredTasks.size());
    }

    @Test
    public void testFilterTasksByContext() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.School));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Errands));

        List<SuccessoratorTask> filteredTasks = SuccessoratorTasksFilterer.filterTasksByContext(TaskContext.Home, tasks);

        assertEquals(1, filteredTasks.size());
        assertEquals("Task 1", filteredTasks.get(0).getName());
    }
}
