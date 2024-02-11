package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTaskRepository;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {

    private final SuccessoratorTaskRepository taskRepository;

    private MainViewModel mainViewModel;
    private final MutableSubject<List<SuccessoratorTask>> orderedTasks;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository());
                    });

    //might make public
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainViewModel.class);
        Button viewTasksButton = findViewById(R.id.viewTasksButton);
        viewTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //need to update tasks somehow
                mainViewModel.getOrderedTasks().observe(MainActivity.this, tasks -> {
                    //enter tasks here
                    for (SuccessoratorTask task : tasks) {
                        //print out each task
                        System.out.println("Task: " + task.getTitle());
                    }
                });
            }
        });
    }
    //regular main view function
    public MainViewModel(SuccessoratorTaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        this.orderedTasks = new SimpleSubject<>();

        taskRepository.findAll().observe(tasks -> {
            if (tasks != null) {

                var newTasks = tasks.stream()
                        .sorted(Comparator.comparingInt(SuccessoratorTask::getSortOrder))
                        .collect(Collectors.toList());
                this.orderedTasks.setValue(newTasks);
            }
        });

        //uncommenting this crashes app
        //orderedTasks.observe(this.orderedTasks::setValue);
    }

    public Subject<List<SuccessoratorTask>> getOrderedTasks() {
        return orderedTasks;
    }

}
