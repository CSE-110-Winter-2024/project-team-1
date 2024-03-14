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

        SuccessoratorTask task = new SuccessoratorTask(id, name, sortOrder, isComplete, TaskType.Normal, 0, TaskContext.Home);

        assertEquals(id, task.getId());
        assertEquals(name, task.getName());
        assertEquals(sortOrder, task.getSortOrder());
        assertEquals(isComplete, task.getIsComplete());
    }

    @Test
    public void testWithId() {
        Integer id = 1;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, TaskContext.Home);

        SuccessoratorTask updatedTask = task.withId(id);

        assertEquals(id, updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getDueDate(), updatedTask.getDueDate());
    }

    @Test
    public void testWithSortOrder() {
        int sortOrder = 2;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, TaskContext.Home);

        SuccessoratorTask updatedTask = task.withSortOrder(sortOrder);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(sortOrder, updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getDueDate(), updatedTask.getDueDate());
    }

    @Test
    public void testWithIsComplete() {
        Boolean isComplete = true;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, TaskContext.Home);

        SuccessoratorTask updatedTask = task.withIsComplete(isComplete);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(isComplete, updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getDueDate(), updatedTask.getDueDate());
    }

    @Test
    public void testWithTaskType() {
        TaskType taskType = TaskType.Normal;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, TaskContext.Home);

        SuccessoratorTask updatedTask = task.withType(taskType);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(taskType, updatedTask.getType());
        assertEquals(task.getDueDate(), updatedTask.getDueDate());
    }

    @Test
    public void testWithDueDate() {
        long dueDate = 0;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 1, TaskContext.Home);

        SuccessoratorTask updatedTask = task.withDueDate(dueDate);

        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(dueDate, updatedTask.getDueDate());
    }

    @Test
    public void testRescheduleDaily() {
        SuccessoratorTask task = new SuccessoratorTask(null, "Daily Task", 0, false, TaskType.Recurring, LocalDate.now().toEpochDay(), TaskContext.Home);
        SuccessoratorRecurringTask recurringTask = new SuccessoratorRecurringTask(null, "Daily Task", 0, LocalDate.now().toEpochDay(), 0, TaskInterval.Daily, TaskContext.Home, 0, 0);
        SuccessoratorTask updatedTask = recurringTask.scheduleTask();
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getDueDate() + 1, updatedTask.getDueDate());
    }

    @Test
    public void testRescheduleWeekly() {
        SuccessoratorTask task = new SuccessoratorTask(null, "Weekly Task", 0, false, TaskType.Recurring, LocalDate.now().toEpochDay(), TaskContext.Home);
        SuccessoratorRecurringTask recurringTask = new SuccessoratorRecurringTask(null, "Weekly Task", 0, LocalDate.now().toEpochDay(), 0, TaskInterval.Weekly, TaskContext.Home, 0, 0);
        SuccessoratorTask updatedTask = recurringTask.scheduleTask();
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getDueDate() + 7, updatedTask.getDueDate());
    }

    @Test
    public void testRescheduleMonthlyHappy() {
        SuccessoratorTask task = new SuccessoratorTask(null, "Monthly Task", 0, false, TaskType.Recurring, 19789, TaskContext.Home);
        SuccessoratorRecurringTask recurringTask = new SuccessoratorRecurringTask(null, "Monthly Task", 0, 19789, 0, TaskInterval.Monthly, TaskContext.Home, 0, 0);
        SuccessoratorTask updatedTask = recurringTask.scheduleTask();

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(19817, updatedTask.getDueDate());
    }

    @Test
    public void testRescheduleYearlyHappy() {
        SuccessoratorTask task = new SuccessoratorTask(null, "Yearly Task", 0, false, TaskType.Recurring, 19790, TaskContext.Home);
        SuccessoratorRecurringTask recurringTask = new SuccessoratorRecurringTask(null, "Yearly Task", 0, 19790, 0, TaskInterval.Yearly, TaskContext.Home, 0, 0);
        SuccessoratorTask updatedTask = recurringTask.scheduleTask();

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(20155, updatedTask.getDueDate());
    }

    @Test
    public void testRescheduleFifthDay() {
        long fifthDayTest = 19812; // March 30th, 2024 (5th Saturday)
        SuccessoratorTask task = new SuccessoratorTask(null, "Fifth Saturday Task", 0, false, TaskType.Recurring, fifthDayTest, TaskContext.Home);
        SuccessoratorRecurringTask recurringTask = new SuccessoratorRecurringTask(null, "Fifth Saturday Task", 0, fifthDayTest, 0, TaskInterval.Monthly, TaskContext.Home, 0, 0);
        SuccessoratorTask updatedTask = recurringTask.scheduleTask();

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(19847, updatedTask.getDueDate());
    }

    @Test
    public void testRescheduleLeapYear() {
        long leapYearTest = 19782; // February 29th, 2024
        SuccessoratorTask task = new SuccessoratorTask(null, "Leap Year Task", 0, false, TaskType.Recurring, leapYearTest, TaskContext.Home);
        SuccessoratorRecurringTask recurringTask = new SuccessoratorRecurringTask(null, "Leap Year Task", 0, leapYearTest, 0, TaskInterval.Monthly, TaskContext.Home, 0, 0);
        SuccessoratorTask updatedTask = recurringTask.scheduleTask();

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(20148, updatedTask.getDueDate());

        // we'll manually check the next four years
        updatedTask = recurringTask.scheduleTask();

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(20513, updatedTask.getDueDate());


        updatedTask = recurringTask.scheduleTask();

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(20878, updatedTask.getDueDate());


        updatedTask = recurringTask.scheduleTask();

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(21243, updatedTask.getDueDate());
    }

    @Test
    public void testWithContext(){
        TaskContext context = TaskContext.School;
        SuccessoratorTask task = new SuccessoratorTask(null, "Task", 0, false, TaskType.Normal, 0, TaskContext.Home);
        SuccessoratorTask updatedTask = task.withContext(context);
        assertNull(updatedTask.getId());
        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getSortOrder(), updatedTask.getSortOrder());
        assertEquals(task.getIsComplete(), updatedTask.getIsComplete());
        assertEquals(task.getType(), updatedTask.getType());
        assertEquals(task.getDueDate(), updatedTask.getDueDate());
        assertEquals(TaskContext.School, updatedTask.getContext());
    }

}