package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SuccessoratorContextFiltererTest {
    @Test
    public void testFilterHome() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Daily, TaskContext.Work));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Errands));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));

        assertEquals(3, tasks.size());
        List<SuccessoratorTask> filteredTasks = SuccessoratorContextFilterer.filterContext(TaskContext.Home, tasks);
        assertEquals(1, filteredTasks.size());
    }

    @Test
    public void testFilterWork() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Daily, TaskContext.Work));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Errands));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));

        assertEquals(3, tasks.size());
        List<SuccessoratorTask> filteredTasks = SuccessoratorContextFilterer.filterContext(TaskContext.Work, tasks);
        assertEquals(1, filteredTasks.size());
    }

    @Test
    public void testFilterSchool() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Daily, TaskContext.Work));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.School));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));

        assertEquals(3, tasks.size());
        List<SuccessoratorTask> filteredTasks = SuccessoratorContextFilterer.filterContext(TaskContext.School, tasks);
        assertEquals(1, filteredTasks.size());
    }

    @Test
    public void testFilterErrands() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Daily, TaskContext.Work));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.School));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Errands));

        assertEquals(3, tasks.size());
        List<SuccessoratorTask> filteredTasks = SuccessoratorContextFilterer.filterContext(TaskContext.School, tasks);
        assertEquals(1, filteredTasks.size());
    }

    @Test
    public void testFilterCancel() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Daily, TaskContext.Work));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.School));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home));

        assertEquals(3, tasks.size());
        List<SuccessoratorTask> filteredTasks = SuccessoratorContextFilterer.filterContext(TaskContext.School, tasks);
        assertEquals(1, filteredTasks.size());
        List<SuccessoratorTask> refilteredTasks = SuccessoratorContextFilterer.filterContext(TaskContext.None, tasks);
        assertEquals(3, refilteredTasks.size());
    }

    @Test
    public void testFilterByHomeWithNoHome() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.School));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Errands));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Work));

        List<SuccessoratorTask> filteredTasks = SuccessoratorContextFilterer.filterContext(TaskContext.Home, tasks);
        assertEquals(0, filteredTasks.size());
    }

    @Test
    public void testFilterByNoneWithNoTasks() {
        List<SuccessoratorTask> tasks = new ArrayList<>();

        List<SuccessoratorTask> filteredTasks = SuccessoratorContextFilterer.filterContext(TaskContext.None, tasks);
        assertEquals(0, filteredTasks.size());
    }
}
