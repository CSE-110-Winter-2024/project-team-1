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
    private TaskContext selectedContext;

    public boolean recurringActive = false;

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

                this.unfilteredTasks.setValue(newTasks);
                applyFilters(newTasks);
            }
        });


        recurringTaskRepository.findAll().observe(recurringTasks -> {
            if (recurringTasks != null) {
                var newRecurringTasks = recurringTasks.stream()
                        .sorted(Comparator.comparingInt(SuccessoratorRecurringTask::getSortOrder))
                        .collect(Collectors.toList());

                this.unfilteredRecurringTasks.setValue(newRecurringTasks);
                applyRecurringFilters(newRecurringTasks);
            }
        });
    }

    public Subject<List<SuccessoratorTask>> getOrderedTasks() {
        return orderedTasks;
    }

    public Subject<List<SuccessoratorRecurringTask>> getOrderedRecurringTasks() {
        return orderedRecurringTasks;
    }

    public void add(SuccessoratorTask task) {
        //taskRepository.add(task);
        // temporary change pending OH consultation
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) { // TODO: this only really happens when we're using InMemory -- does not address the root cause
            taskRepository.add(task);
            return;
        }
        var newTasks = SuccessoratorTasks.insertByContext(tasks, task, false);
        taskRepository.save(newTasks);
    }

    public void add(SuccessoratorRecurringTask task) {
        System.out.println("Add method called!");
        var tasks = this.unfilteredRecurringTasks.getValue();
        if (tasks == null) {
            recurringTaskRepository.add(task);
            return;
        }
        var newTasks = SuccessoratorTasks.insertTask(tasks, task, true);
        recurringTaskRepository.save(newTasks);

        System.out.println("Recurring creaated!");

        var normalTasks = this.unfilteredTasks.getValue();
        if (normalTasks == null) {
            return;
        }
        System.out.println("Calling schedule method!");
        var newNormalTasks = SuccessoratorTasks.scheduleTasks(normalTasks, task);
        taskRepository.save(newNormalTasks);
    }

    public void removeTask(int sortOrder) {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.deleteTask(tasks, sortOrder);
        taskRepository.save(newTasks);
    }

    public void removeRecurringTask(int sortOrder) {
        var tasks = this.unfilteredRecurringTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.deleteRecurringTask(tasks, sortOrder);
        recurringTaskRepository.save(newTasks);
    }

    public void rescheduleTaskToToday(int sortOrder) {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.rescheduleTaskToToday(tasks, sortOrder);
        taskRepository.save(newTasks);
    }

    public void rescheduleTaskToTomorrow(int sortOrder) {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.rescheduleTaskToTomorrow(tasks, sortOrder);
        taskRepository.save(newTasks);
    }

    public void markComplete(int sortOrder) {
        var tasks = this.unfilteredTasks.getValue();
        var newTasks = SuccessoratorTasks.toggleComplete(tasks, sortOrder);
        taskRepository.save(newTasks);
    }

    public void removeFinishedTasks(long date) {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.removeCompletedTasks(tasks, date);
        taskRepository.save(newTasks);
    }

    public void rescheduleTasks() {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        var newTasks = SuccessoratorTasks.rescheduleTasks(tasks);
        taskRepository.save(newTasks);
    }

    public void changeFilter(TaskFilterOption filter) {
        this.selectedFilter = filter;
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            android.util.Log.d("tasks", "is null");
            return;
        }
        
        applyFilters(tasks);
    }

    public TaskFilterOption getSelectedFilter() {
        return selectedFilter;
    }

    private void applyFilters(List<SuccessoratorTask> tasks) {
        var newTasks = SuccessoratorTasksFilterer.filterTasks(selectedFilter, tasks);
        if (selectedContext != null) {
            newTasks = SuccessoratorTasksFilterer.filterTasksByContext(selectedContext, newTasks);
        }
        if (selectedFilter == TaskFilterOption.Recurring) {
            this.recurringActive = true;
        } else {
            this.recurringActive = false;
        }
        this.orderedTasks.setValue(newTasks);
    }

    private void applyRecurringFilters(List<SuccessoratorRecurringTask> tasks) {
        var newTasks = tasks;
        if (selectedFilter != TaskFilterOption.Recurring) {
            this.recurringActive = false;
        }
        else {
            this.recurringActive = true;
        }
        if (selectedContext != null) {
            newTasks = SuccessoratorTasksFilterer.filterRecurringTasksByContext(selectedContext, newTasks);
        }
        this.orderedRecurringTasks.setValue(newTasks);
    }

    public void focus(String focusContext) {
        var tasks = this.unfilteredTasks.getValue();
        if (tasks == null) {
            return;
        }
        if (!focusContext.equals("Cancel")) {
            // focus mode is sticky
            selectedContext = TaskContext.valueOf(focusContext);
            tasks = SuccessoratorTasksFilterer.filterTasksByContext(selectedContext, tasks);
        }
        else {
            selectedContext = null;
        }
        var newTasks = SuccessoratorTasksFilterer.filterTasks(selectedFilter, tasks);
        this.orderedTasks.setValue(newTasks);

        //also filter recurring tasks
        var recurringTasks = this.unfilteredRecurringTasks.getValue();
        if (recurringTasks == null) {
            return;
        }
        applyRecurringFilters(recurringTasks);
    }
}