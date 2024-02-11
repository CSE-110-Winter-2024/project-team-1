package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class SuccessoratorTaskTest {

    @Test
    public void testConstructAndGetters() {
        Integer id = 1;
        String name = "Task 1";
        int sortOrder = 1;
        Boolean isComplete = false;

        SuccessoratorTask task = new SuccessoratorTask(id, name, sortOrder, isComplete);

        assertEquals(id, task.getId());
        assertEquals(name, task.getName());
        assertEquals(sortOrder, task.getSortOrder());
        assertEquals(isComplete, task.getIsComplete());
    }

    @Test
    public void testWithId() {
        Integer id = 1;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false);

        SuccessoratorTask updatedTask = task.withId(id);

        assertEquals(id, updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
    }

    @Test
    public void testWithSortOrder() {
        int sortOrder = 2;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false);

        SuccessoratorTask updatedTask = task.withSortOrder(sortOrder);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(sortOrder, updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
    }

    @Test
    public void testWithIsComplete() {
        Boolean isComplete = true;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false);

        SuccessoratorTask updatedTask = task.withIsComplete(isComplete);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(isComplete, updatedTask.getIsComplete());
    }
}