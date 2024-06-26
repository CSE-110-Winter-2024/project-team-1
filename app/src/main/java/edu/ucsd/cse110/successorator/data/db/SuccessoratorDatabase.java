package edu.ucsd.cse110.successorator.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTask;

@Database(entities = {SuccessoratorTaskEntity.class, SuccessoratorRecurringTaskEntity.class}, version = 1)
public abstract class SuccessoratorDatabase extends RoomDatabase {
    public abstract SuccessoratorTaskDao taskDao();
    public abstract SuccessoratorRecurringTaskDao recurringTaskDao();
}
