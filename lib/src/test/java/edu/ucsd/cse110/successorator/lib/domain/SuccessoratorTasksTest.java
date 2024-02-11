package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SuccessoratorTasksTest {
    @Test
    public void testToggleCompleteStart() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 0);

        assertEquals(true, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(2).getIsComplete().booleanValue());
    }

    @Test
    public void testToggleCompleteMiddle() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 1);

        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(true, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(2).getIsComplete().booleanValue());
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
    }

    @Test
    public void testToggleCompleteEmpty() {
        // catch the illegal argument exception
        List<SuccessoratorTask> tasks = new ArrayList<>();

        try {
            SuccessoratorTasks.toggleComplete(tasks, 0);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sortOrder", e.getMessage());

        }
    }

    @Test
    public void testToggleCompleteInvalidIndex() {
        // catch the illegal argument exception
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false));

        try {
            SuccessoratorTasks.toggleComplete(tasks, 3);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sortOrder", e.getMessage());
        }
    }

}