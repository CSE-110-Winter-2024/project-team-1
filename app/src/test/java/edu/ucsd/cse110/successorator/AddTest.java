package edu.ucsd.cse110.successorator;

import junit.framework.TestCase;

import org.junit.jupiter.api.Test;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.InMemorySuccessoratorTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class AddTest extends TestCase {
    @Test
    public void testAddMethod() {
        SuccessoratorTaskRepository taskRepository = new InMemorySuccessoratorTaskRepository();

        MainViewModel viewModel = new MainViewModel(taskRepository);

        SuccessoratorTask taskToAdd = new SuccessoratorTask(1, "test", 1, false);

        viewModel.add(taskToAdd);

        Subject<SuccessoratorTask> foundTaskSubject = taskRepository.find(taskToAdd.getId());
        boolean found = foundTaskSubject != null;

        assertTrue(found);
    }
}