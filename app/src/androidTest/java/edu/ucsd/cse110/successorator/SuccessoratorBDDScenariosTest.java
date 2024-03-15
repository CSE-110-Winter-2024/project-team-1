package edu.ucsd.cse110.successorator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class SuccessoratorBDDScenariosTest {
    private SuccessoratorTaskRepository taskRepository;
    private SuccessoratorRecurringTaskRepository recurringTaskRepository;

    @Test
    void filterTaskByCategory() {
        taskRepository = mock(SuccessoratorTaskRepository.class);
        recurringTaskRepository = mock(SuccessoratorRecurringTaskRepository.class);
        // Creating ViewModel with mocked repositories
        MainViewModel model = new MainViewModel(taskRepository, recurringTaskRepository);
    }
    @Test
    void createTaskWithContext() {
        // Mocking repositories
        taskRepository = mock(SuccessoratorTaskRepository.class);
        recurringTaskRepository = mock(SuccessoratorRecurringTaskRepository.class);
        // Creating ViewModel with mocked repositories
        MainViewModel model = new MainViewModel(taskRepository, recurringTaskRepository);
        // WHEN
        model.add(new SuccessoratorTask(1, "taskText", 1, false, TaskType.Normal, 0, TaskContext.Errands));
        // THEN
        List<SuccessoratorTask> tasks = model.getOrderedTasks().getValue();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(TaskContext.Errands, tasks.get(0).getContext());
    }

    @Test
    void filterTasksByContext() {
        // Given the user is on any task view
        // When the user selects the hamburger menu icon
        // And the user selects a context
        // Then only the tasks with that context show up on task view
        // And the top bar has a focus mode indicator
    }

    @Test
    void createRecurringTask() {
        // Given the user is on the view after pressing the plus button
        // When the user selects an option for the task (daily, monthly, etc.)
        // And the user selects create
        // Then the task appears on the task view, and it keeps showing up whenever it occurs.
    }

    @Test
    void moveAndEditTasks() {
        // Given the user is on the task view and there is a task present
        // When the user long presses on the task
        // Then a menu for moving, finishing, and deleting the task should appear.
    }

    @Test
    void seeCurrentTitleAtTopOfScreen() {
        // Given the user is on the task view
        // When the user selects the Today or Tomorrow views
        // Then the Today or Tomorrow text, along with the correct date, shows up as the title.
    }

    @Test
    void addNewTasksToCategory() {
        // Given the user is on the task view of any context
        // When the user clicks the plus button
        // Then the user is prompted with a view to input a task with a save button
    }
}
