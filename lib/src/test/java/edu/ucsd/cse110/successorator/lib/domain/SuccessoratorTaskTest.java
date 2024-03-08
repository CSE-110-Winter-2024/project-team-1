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

        SuccessoratorTask task = new SuccessoratorTask(id, name, sortOrder, isComplete, TaskType.Normal, 0, 0, TaskInterval.Daily);

        assertEquals(id, task.getId());
        assertEquals(name, task.getName());
        assertEquals(sortOrder, task.getSortOrder());
        assertEquals(isComplete, task.getIsComplete());
    }

    @Test
    public void testWithId() {
        Integer id = 1;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily);

        SuccessoratorTask updatedTask = task.withId(id);

        assertEquals(id, updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
    }

    @Test
    public void testWithSortOrder() {
        int sortOrder = 2;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily);

        SuccessoratorTask updatedTask = task.withSortOrder(sortOrder);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(sortOrder, updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
    }

    @Test
    public void testWithIsComplete() {
        Boolean isComplete = true;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily);

        SuccessoratorTask updatedTask = task.withIsComplete(isComplete);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(isComplete, updatedTask.getIsComplete());
    }

    @Test
    public void testWithTaskType() {
        TaskType taskType = TaskType.Normal;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily);

        SuccessoratorTask updatedTask = task.withType(taskType);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(taskType, updatedTask.getType());
    }

    @Test
    public void testWithDueDate() {
        long dueDate = 0;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 1, 1, TaskInterval.Daily);

        SuccessoratorTask updatedTask = task.withDueDate(dueDate);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(dueDate, updatedTask.getDueDate());
    }
}