package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;

@Dao
public interface SuccessoratorRecurringTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(SuccessoratorRecurringTaskEntity task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<SuccessoratorRecurringTaskEntity> tasks);

    @Query("SELECT * FROM successoratorRecurringTasks WHERE id = :id")
    SuccessoratorRecurringTaskEntity find(int id);

    @Query("SELECT * FROM successoratorRecurringTasks ORDER BY sort_order")
    List<SuccessoratorRecurringTaskEntity> findAll();


    @Query("SELECT * FROM successoratorRecurringTasks WHERE id = :id")
    LiveData<SuccessoratorRecurringTaskEntity> findAsLiveData(int id);

    @Query("SELECT * FROM successoratorRecurringTasks ORDER BY sort_order")
    LiveData<List<SuccessoratorRecurringTaskEntity>> findAllAsLiveData();

    @Transaction
    default int add(SuccessoratorRecurringTask task) {
        // tasks are always added to the back
        var newTask = new SuccessoratorRecurringTaskEntity(task.getName(), getMaxSortOrder() + 1, task.getCreateDate(), task.getScheduleCount(), task.getInterval().name(), task.getContext().name(), task.getCurrentTask(), task.getUpcomingTask());
        // insert the new task
        return  Math.toIntExact(insert(newTask));
    }

    @Query("SELECT COUNT(*) FROM successoratorRecurringTasks")
    int count();

    @Query("SELECT MIN(sort_order) FROM successoratorRecurringTasks")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM successoratorRecurringTasks")
    int getMaxSortOrder();

    @Query("UPDATE successoratorRecurringTasks SET sort_order = sort_order + :by WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrders(int from, int to, int by);

    @Update
    void update(SuccessoratorRecurringTaskEntity task);

    @Query("DELETE FROM successoratorRecurringTasks")
    void deleteAll();

    @Query("SELECT max(id) FROM successoratorTasks")
    int getMaxID();
}
