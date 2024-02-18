package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;

@Dao
    public interface SuccessoratorTaskDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        Long insert(SuccessoratorTaskEntity flashcard);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        List<Long> insert(List<SuccessoratorTaskEntity> flashcards);

        @Query("SELECT * FROM successoratorTasks WHERE id = :id")
        SuccessoratorTaskEntity find(int id);

        @Query("SELECT * FROM successoratorTasks ORDER BY sort_order")
        List<SuccessoratorTaskEntity> findAll();


        @Query("SELECT * FROM successoratorTasks WHERE id = :id")
        LiveData<SuccessoratorTaskEntity> findAsLiveData(int id);

        @Query("SELECT * FROM successoratorTasks ORDER BY sort_order")
        LiveData<List<SuccessoratorTaskEntity>> findAllAsLiveData();

        @Transaction
        default int add(SuccessoratorTask task) {
            // tasks are always added to the back
            var newTask = new SuccessoratorTaskEntity(task.getName(), getMaxSortOrder() + 1, false, task.getDate());
            // insert the new task
            return  Math.toIntExact(insert(newTask));
        }

    @Transaction
    default int markComplete(int id, int completedTasksBeginIndex) {
        // find the task
        SuccessoratorTaskEntity taskEntity = find(id);
        if (taskEntity == null) {
            return -1; // Task not found
        }

        var task = taskEntity.toTask();
        // Get current sort order of the task
        int currentSortOrder = task.getSortOrder();

        // Get the sort order for the completed index

        // Update the task as completed
        var newTask = new SuccessoratorTask(task.getId(), task.getName(), completedTasksBeginIndex - 1, true);

        // Shift sort orders for tasks between currentSortOrder and completedSortOrder
        shiftSortOrders(getMinSortOrder(), completedTasksBeginIndex - 1, -1);


        // Update the sort order of the completed task to move it to the completed index

        // Update the database with the modified task
        update(SuccessoratorTaskEntity.fromTask(newTask));

        return 0;
        }

        @Query("SELECT COUNT(*) FROM successoratorTasks")
        int count();

        @Query("SELECT MIN(sort_order) FROM successoratorTasks")
        int getMinSortOrder();

        @Query("SELECT MAX(sort_order) FROM successoratorTasks")
        int getMaxSortOrder();

        @Query("UPDATE successoratorTasks SET sort_order = sort_order + :by WHERE sort_order >= :from AND sort_order <= :to")
        void shiftSortOrders(int from, int to, int by);

        @Update
        void update(SuccessoratorTaskEntity flashcard);

}
