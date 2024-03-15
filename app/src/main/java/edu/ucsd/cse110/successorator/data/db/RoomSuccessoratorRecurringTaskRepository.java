package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorRecurringTask;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class RoomSuccessoratorRecurringTaskRepository implements SuccessoratorRecurringTaskRepository {
    private final SuccessoratorRecurringTaskDao dao;

    public RoomSuccessoratorRecurringTaskRepository(SuccessoratorRecurringTaskDao dao) {
        this.dao = dao;
    }

    @Override
    public Subject<SuccessoratorRecurringTask> find(int id) {
        LiveData<SuccessoratorRecurringTaskEntity> entityLiveData = dao.findAsLiveData(id);
        LiveData<SuccessoratorRecurringTask> taskLiveData = Transformations.map(entityLiveData, SuccessoratorRecurringTaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public Subject<List<SuccessoratorRecurringTask>> findAll() {
        LiveData<List<SuccessoratorRecurringTaskEntity>> entityLiveData = dao.findAllAsLiveData();
        LiveData<List<SuccessoratorRecurringTask>> taskLiveData = Transformations.map(entityLiveData, entities -> {
            return entities.stream()
                    .map(SuccessoratorRecurringTaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public void add(SuccessoratorRecurringTask task) {
        dao.add(task);
    }

    @Override
    public void save(List<SuccessoratorRecurringTask> tasks) {
        dao.deleteAll();
        var entities = tasks.stream()
                .map(SuccessoratorRecurringTaskEntity::fromTask)
                .collect(Collectors.toList());
        dao.insert(entities);
    }

    @Override
    public void save(SuccessoratorRecurringTask task) {
        dao.insert(SuccessoratorRecurringTaskEntity.fromTask(task));
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public int getMaxID() {
        return dao.getMaxID();
    }
}
