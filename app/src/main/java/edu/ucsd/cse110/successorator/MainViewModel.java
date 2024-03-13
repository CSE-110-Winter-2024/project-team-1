package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTasks;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTasksFilterer;
import edu.ucsd.cse110.successorator.lib.domain.TaskFilterOption;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {

    private final SuccessoratorTaskRepository taskRepository;

    private final MutableSubject<List<SuccessoratorTask>> orderedTasks;

    private final MutableSubject<List<SuccessoratorTask>> unfilteredTasks;

    private TaskFilterOption selectedFilter = TaskFilterOption.Today;
    private TaskContext selectedContext;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository());
                    });

    public MainViewModel(SuccessoratorTaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        this.orderedTasks = new SimpleSubject<>();

        this.unfilteredTasks = new SimpleSubject<>();

        taskRepository.findAll().observe(tasks -> {
            if (tasks != null) {

                var newTasks = tasks.stream()
                        .sorted(Comparator.comparingInt(SuccessoratorTask::getSortOrder))
                        .collect(Collectors.toList());

                var filtertedTasks = SuccessoratorTasksFilterer.filterTasks(selectedFilter, newTasks);
                this.orderedTasks.setValue(filtertedTasks);
                this.unfilteredTasks.setValue(newTasks);
            }
        });
        this.selectedContext = TaskContext.Home;
    }

    public Subject<List<SuccessoratorTask>> getOrderedTasks() {
        return orderedTasks;
    }

    public void add(SuccessoratorTask task) {
        //taskRepository.add(task);
        // temporary change pending OH consultation
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) { // TODO: this only really happens when we're using InMemory -- does not address the root cause
            taskRepository.add(task);
            return;
        }
        var newTasks = SuccessoratorTasks.insertTask(tasks, task, true);
        taskRepository.save(newTasks);
    }

    public void markComplete(int sortOrder) {
        var tasks = this.orderedTasks.getValue();
        var newTasks = SuccessoratorTasks.toggleComplete(tasks, sortOrder);
        taskRepository.save(newTasks);
        this.orderedTasks.setValue(newTasks);
    }

    public void removeFinishedTasks() {
        var tasks = this.orderedTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.removeCompletedTasks(tasks);
        taskRepository.save(newTasks);
        this.orderedTasks.setValue(newTasks);
    }

    public void rescheduleTasks() {
        var tasks = this.orderedTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.rescheduleTasks(tasks);
        taskRepository.save(newTasks);
        this.orderedTasks.setValue(newTasks);
    }

    public void changeFilter(TaskFilterOption filter) {
        this.selectedFilter = filter;
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            return;
        }
        var newTasks = SuccessoratorTasksFilterer.filterTasks(filter, tasks);
        this.orderedTasks.setValue(newTasks);
    }

    public TaskFilterOption getSelectedFilter() {
        return selectedFilter;
    }

    public void changeContext(TaskContext context) {
        this.selectedContext = context;
    }

    public TaskContext getSelectedContext() {
        return selectedContext;
    }
}