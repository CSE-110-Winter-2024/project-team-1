package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTask;
import edu.ucsd.cse110.successorator.lib.domain.SuccessoratorTaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class RoomSuccessoratorTaskRepository implements SuccessoratorTaskRepository {
    private final SuccessoratorTaskDao dao;

    public RoomSuccessoratorTaskRepository(SuccessoratorTaskDao dao) {
        this.dao = dao;
    }

    @Override
    public Subject<SuccessoratorTask> find(int id) {
        LiveData<SuccessoratorTaskEntity> entityLiveData = dao.findAsLiveData(id);
        LiveData<SuccessoratorTask> taskLiveData = Transformations.map(entityLiveData, SuccessoratorTaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public Subject<List<SuccessoratorTask>> findAll() {
        LiveData<List<SuccessoratorTaskEntity>> entityLiveData = dao.findAllAsLiveData();
        LiveData<List<SuccessoratorTask>> taskLiveData = Transformations.map(entityLiveData, entities -> {
            return entities.stream()
                    .map(SuccessoratorTaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public void add(SuccessoratorTask task) {
        dao.add(task);
    }

    @Override
    public void save(List<SuccessoratorTask> tasks) {
        dao.deleteAll();
        var entities = tasks.stream()
                .map(SuccessoratorTaskEntity::fromTask)
                .collect(Collectors.toList());
        dao.insert(entities);
    }

    @Override
    public void save(SuccessoratorTask task) {
        dao.insert(SuccessoratorTaskEntity.fromTask(task));
    }

    @Override
    public void markComplete(int id, int completedTasksBeginIndex) {
        dao.markComplete(id, completedTasksBeginIndex);
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}
