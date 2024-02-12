package edu.ucsd.cse110.successorator.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTasks;

public class SuccessoratorTasksTest {
    @Test
    public void testToggleCompleteStart() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 0);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(2).getIsComplete().booleanValue());
        assertEquals("Task 1", modifiedTasks.get(2).getName());
    }

    @Test
    public void testToggleCompleteMiddle() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 1);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(2).getIsComplete().booleanValue());
        assertEquals("Task 2", modifiedTasks.get(2).getName());
    }

    @Test
    public void testToggleCompleteEnd() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 2);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(2).getIsComplete().booleanValue());
        assertEquals("Task 3", modifiedTasks.get(2).getName());
    }

    @Test
    public void testToggleCompleteTwice() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 1);
        modifiedTasks = SuccessoratorTasks.toggleComplete(modifiedTasks, 1);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(2).getIsComplete().booleanValue());
        assertEquals("Task 3", modifiedTasks.get(1).getName());
        assertEquals("Task 2", modifiedTasks.get(2).getName());
    }
}