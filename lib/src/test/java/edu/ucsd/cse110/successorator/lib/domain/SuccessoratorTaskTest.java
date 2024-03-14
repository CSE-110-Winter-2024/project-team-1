package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;

public class SuccessoratorTaskTest {

    @Test
    public void testConstructAndGetters() {
        Integer id = 1;
        String name = "Task 1";
        int sortOrder = 1;
        Boolean isComplete = false;

        SuccessoratorTask task = new SuccessoratorTask(id, name, sortOrder, isComplete, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home);

        assertEquals(id, task.getId());
        assertEquals(name, task.getName());
        assertEquals(sortOrder, task.getSortOrder());
        assertEquals(isComplete, task.getIsComplete());
    }

    @Test
    public void testWithId() {
        Integer id = 1;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home);

        SuccessoratorTask updatedTask = task.withId(id);

        assertEquals(id, updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(task.getDueDate(), updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
    }

    @Test
    public void testWithSortOrder() {
        int sortOrder = 2;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home);

        SuccessoratorTask updatedTask = task.withSortOrder(sortOrder);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(sortOrder, updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(task.getDueDate(), updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
    }

    @Test
    public void testWithIsComplete() {
        Boolean isComplete = true;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home);

        SuccessoratorTask updatedTask = task.withIsComplete(isComplete);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(isComplete, updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(task.getDueDate(), updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
    }

    @Test
    public void testWithTaskType() {
        TaskType taskType = TaskType.Normal;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home);

        SuccessoratorTask updatedTask = task.withType(taskType);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(taskType, updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(task.getDueDate(), updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
    }

    @Test
    public void testWithDueDate() {
        long dueDate = 0;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 1, 1, TaskInterval.Daily, TaskContext.Home);

        SuccessoratorTask updatedTask = task.withDueDate(dueDate);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(dueDate, updatedTask.getDueDate());
    }

    @Test
    public void testRescheduleDaily() {
        SuccessoratorTask task = new SuccessoratorTask(null, "Daily Task", 0, false, TaskType.Recurring, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Daily, TaskContext.Home);
        SuccessoratorTask updatedTask = SuccessoratorTasks.rescheduleTask(task);
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(task.getDueDate() + 1, updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
    }

    @Test
    public void testRescheduleWeekly() {
        SuccessoratorTask task = new SuccessoratorTask(null, "Weekly Task", 0, false, TaskType.Recurring, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay(), TaskInterval.Weekly, TaskContext.Home);
        SuccessoratorTask updatedTask = SuccessoratorTasks.rescheduleTask(task);
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(task.getDueDate() + 7, updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
    }

    @Test
    public void testRescheduleMonthlyHappy() {
        SuccessoratorTask task = new SuccessoratorTask(null, "Monthly Task", 0, false, TaskType.Recurring, 19789, 19789, TaskInterval.Monthly, TaskContext.Home);
        SuccessoratorTask updatedTask = SuccessoratorTasks.rescheduleTask(task);

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(19817, updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
    }

    @Test
    public void testRescheduleYearlyHappy() {
        SuccessoratorTask task = new SuccessoratorTask(null, "Yearly Task", 0, false, TaskType.Recurring, 19790, 19790, TaskInterval.Yearly, TaskContext.Home);
        SuccessoratorTask updatedTask = SuccessoratorTasks.rescheduleTask(task);

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(20155, updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
    }

    @Test
    public void testRescheduleFifthDay() {
        long fifthDayTest = 19812; // March 30th, 2024 (5th Saturday)
        SuccessoratorTask task = new SuccessoratorTask(null, "Fifth Saturday Task", 0, false, TaskType.Recurring, fifthDayTest, fifthDayTest, TaskInterval.Monthly, TaskContext.Home);
        SuccessoratorTask updatedTask = SuccessoratorTasks.rescheduleTask(task);

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(19847, updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
    }

    @Test
    public void testRescheduleLeapYear() {
        long leapYearTest = 19782; // February 29th, 2024
        SuccessoratorTask task = new SuccessoratorTask(null, "Fifth Saturday Task", 0, false, TaskType.Recurring, leapYearTest, leapYearTest, TaskInterval.Yearly, TaskContext.Home);
        SuccessoratorTask updatedTask = SuccessoratorTasks.rescheduleTask(task);

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(20148, updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());

        // we'll manually check the next four years
        updatedTask = SuccessoratorTasks.rescheduleTask(updatedTask);

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(20513, updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());


        updatedTask = SuccessoratorTasks.rescheduleTask(updatedTask);

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(20878, updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());


        updatedTask = SuccessoratorTasks.rescheduleTask(updatedTask);

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(21243, updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
    }

    @Test
    public void testWithContext(){
        TaskContext context = TaskContext.School;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, 0, TaskInterval.Daily, TaskContext.Home);
        SuccessoratorTask updatedTask = task.withContext(context);
        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getCreateDate(), updatedTask.getCreateDate());
        assertEquals(task.getDueDate(), updatedTask.getDueDate());
        assertEquals(task.getInterval(), updatedTask.getInterval());
        assertEquals(TaskContext.School, updatedTask.getContext());
    }
}