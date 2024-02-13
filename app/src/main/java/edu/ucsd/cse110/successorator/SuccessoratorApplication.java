package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.data.db.RoomSuccessoratorTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTaskRepository;

public class SuccessoratorApplication extends Application {
    private SuccessoratorTaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        var database = Room.databaseBuilder(
                getApplicationContext(),
                SuccessoratorDatabase.class,
                "successorator-database"
        )
                .allowMainThreadQueries()
                .build();

        this.taskRepository = new RoomSuccessoratorTaskRepository(database.taskDao());

        // populate tasks for testing, delete after adding US-4
        /*var testTasks = java.util.List.of(
                new SuccessoratorTask(0, "task 1", 0, false),
                new SuccessoratorTask(1, "task 2", 1, false),
                new SuccessoratorTask(2, "task 3", 2, false),
                new SuccessoratorTask(3, "task 4", 3, false),
                new SuccessoratorTask(4, "task 5", 4, false)
        );
        this.taskRepository.save(testTasks);*/
    }

    public SuccessoratorTaskRepository getTaskRepository() {
        return taskRepository;
    }
}
