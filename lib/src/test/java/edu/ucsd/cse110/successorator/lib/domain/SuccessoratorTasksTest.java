package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SuccessoratorTasksTest {
    @Test
    public void testToggleComplete() {
        List<SuccessoratorTask> tasks = new ArrayList<>();
        tasks.add(new SuccessoratorTask(1, "Task 1", 0, false));
        tasks.add(new SuccessoratorTask(2, "Task 2", 1, false));
        tasks.add(new SuccessoratorTask(3, "Task 3", 2, false));

        List<SuccessoratorTask> modifiedTasks = SuccessoratorTasks.toggleComplete(tasks, 1);

        assertEquals(true, modifiedTasks.get(1).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(0).getIsComplete().booleanValue());
        assertEquals(false, modifiedTasks.get(2).getIsComplete().booleanValue());
    }
}