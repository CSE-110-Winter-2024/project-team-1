package edu.ucsd.cse110.successorator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import edu.ucsd.cse110.successorator.data.db.SuccessoratorTaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskType;

public class SuccessoratorTaskDaoTest {
    private InMemorySuccessoratorTaskDao dao;

    @Before
    public void setUp() {
        dao = new InMemorySuccessoratorTaskDao();
    }

    @Test
    public void testInsert() {
        SuccessoratorTaskEntity task = new SuccessoratorTaskEntity("Task Name", 1, false, TaskType.Normal.name(), 0, null);
        int id = dao.insert(task);
        assertNotNull(id);
    }

    @Test
    public void testFind() {
        SuccessoratorTaskEntity task = new SuccessoratorTaskEntity("Task Name", 1, false, TaskType.Normal.name(), 0, null);
        int id = dao.insert(task);
        SuccessoratorTaskEntity retrievedTask = dao.find(id);
        assertNotNull(retrievedTask);
    }

    @Test
    public void testFindAll() {
        List<SuccessoratorTaskEntity> tasks = createTestTasks(5);
        for (SuccessoratorTaskEntity task : tasks) {
            dao.insert(task);
        }
        List<SuccessoratorTaskEntity> retrievedTasks = dao.findAll();
        assertEquals(5, retrievedTasks.size());
    }

    @Test
    public void testDeleteAll() {
        List<SuccessoratorTaskEntity> tasks = createTestTasks(5);
        for (SuccessoratorTaskEntity task : tasks) {
            dao.insert(task);
        }
        dao.tasks.clear();
        List<SuccessoratorTaskEntity> retrievedTasks = dao.findAll();
        assertEquals(0, retrievedTasks.size());
    }

    private List<SuccessoratorTaskEntity> createTestTasks(int count) {
        List<SuccessoratorTaskEntity> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SuccessoratorTaskEntity task = new SuccessoratorTaskEntity("Task " + i, i, false, TaskType.Normal.name(), 0, null);
            tasks.add(task);
        }
        return tasks;
    }

    private static class InMemorySuccessoratorTaskDao {
        private Map<Integer, SuccessoratorTaskEntity> tasks;
        private int nextId;

        public InMemorySuccessoratorTaskDao() {
            tasks = new HashMap<>();
            nextId = 1;
        }

        public int insert(SuccessoratorTaskEntity task) {
            int id = nextId++;
            task.id = id;
            tasks.put(id, task);
            return id;
        }

        public SuccessoratorTaskEntity find(int id) {
            return tasks.get(id);
        }

        public List<SuccessoratorTaskEntity> findAll() {
            return new ArrayList<>(tasks.values());
        }
    }
}
