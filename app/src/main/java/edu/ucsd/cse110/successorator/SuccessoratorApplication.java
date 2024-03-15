package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.data.db.RoomSuccessoratorRecurringTaskRepository;
import edu.ucsd.cse110.successorator.data.db.RoomSuccessoratorTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTaskRepository;

public class SuccessoratorApplication extends Application {
    private SuccessoratorTaskRepository taskRepository;
    private SuccessoratorRecurringTaskRepository recurringRepository;

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
        this.recurringRepository = new RoomSuccessoratorRecurringTaskRepository(database.recurringTaskDao());
    }

    public SuccessoratorTaskRepository getTaskRepository() {
        return taskRepository;
    }

    public SuccessoratorRecurringTaskRepository getRecurringTaskRepository() {
        return recurringRepository;
    }
}
