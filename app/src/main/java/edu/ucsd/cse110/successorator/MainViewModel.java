package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTaskRepository;
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
    private final SuccessoratorRecurringTaskRepository recurringTaskRepository;

    private final MutableSubject<List<SuccessoratorTask>> orderedTasks;
    private final MutableSubject<List<SuccessoratorTask>> unfilteredTasks;

    private final MutableSubject<List<SuccessoratorRecurringTask>> orderedRecurringTasks;
    private final MutableSubject<List<SuccessoratorRecurringTask>> unfilteredRecurringTasks;

    private TaskFilterOption selectedFilter = TaskFilterOption.Today;
    private TaskContext selectedContext = TaskContext.Home;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository(), app.getRecurringTaskRepository());
                    });

    public MainViewModel(SuccessoratorTaskRepository taskRepository, SuccessoratorRecurringTaskRepository recurringTaskRepository) {
        this.taskRepository = taskRepository;
        this.recurringTaskRepository = recurringTaskRepository;

        this.orderedTasks = new SimpleSubject<>();
        this.unfilteredTasks = new SimpleSubject<>();

        this.orderedRecurringTasks = new SimpleSubject<>();
        this.unfilteredRecurringTasks = new SimpleSubject<>();

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

        recurringTaskRepository.findAll().observe(recurringTasks -> {
            if (recurringTasks != null) {
                var newRecurringTasks = recurringTasks.stream()
                        .sorted(Comparator.comparingInt(SuccessoratorRecurringTask::getSortOrder))
                        .collect(Collectors.toList());

                //var filteredTasks = SuccessoratorTasksFilterer.filterTasks(selectedFilter, newRecurringTasks);
                //this.orderedRecurringTasks.setValue(filteredTasks);
                this.unfilteredRecurringTasks.setValue(newRecurringTasks);
            }
        });
    }

    public Subject<List<SuccessoratorTask>> getOrderedTasks() {
        return orderedTasks;
    }

    public Subject<List<SuccessoratorRecurringTask>> getOrderedRecurringTasks() {
        return unfilteredRecurringTasks; // we dont have ordered tasks yet
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

    public void add(SuccessoratorRecurringTask task) {
        var tasks = this.unfilteredRecurringTasks.getValue();
        if (tasks == null) {
            recurringTaskRepository.add(task);
            return;
        }
        var newTasks = SuccessoratorTasks.insertTask(tasks, task, true);
        recurringTaskRepository.save(newTasks);
    }

    public void removeTask(int sortOrder) {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.deleteTask(tasks, sortOrder);
        taskRepository.save(newTasks);

        applyFilter(newTasks);
    }

    public void removeRecurringTask(int sortOrder) {
        var tasks = this.unfilteredRecurringTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.deleteRecurringTask(tasks, sortOrder);
        recurringTaskRepository.save(newTasks);

        //applyFilter(newTasks);
    }

    public void rescheduleTaskToToday(int sortOrder) {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.rescheduleTaskToToday(tasks, sortOrder);
        taskRepository.save(newTasks);

        applyFilter(newTasks);
    }

    public void rescheduleTaskToTomorrow(int sortOrder) {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.rescheduleTaskToTomorrow(tasks, sortOrder);
        taskRepository.save(newTasks);

        applyFilter(newTasks);
    }

    public void markComplete(int sortOrder) {
        var tasks = this.unfilteredTasks.getValue();
        var newTasks = SuccessoratorTasks.toggleComplete(tasks, sortOrder);
        taskRepository.save(newTasks);

        applyFilter(newTasks);
    }

    public void removeFinishedTasks() {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.removeCompletedTasks(tasks);
        taskRepository.save(newTasks);

        applyFilter(newTasks);
    }

    public void rescheduleTasks() {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.rescheduleTasks(tasks);
        taskRepository.save(newTasks);

        applyFilter(newTasks);
    }

    public void changeFilter(TaskFilterOption filter) {
        this.selectedFilter = filter;
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        
        applyFilter(tasks);
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

    private void applyFilter(List<SuccessoratorTask> tasks) {
        var newTasks = SuccessoratorTasksFilterer.filterTasks(selectedFilter, tasks);
        this.orderedTasks.setValue(newTasks);
    }
}