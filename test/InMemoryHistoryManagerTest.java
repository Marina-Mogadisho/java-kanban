import managers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void init() throws IOException {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
    }

    @Test
    void testAddHistory() throws ManagerSaveException {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
        manager.addTask(task1);
        Task task2 = new Task("Task 2", "Description task 2", Status.IN_PROGRESS, "15", "11:55 14.02.2025");
        manager.addTask(task2);
        final Task savedTask = manager.getTaskById(task1.getId());
        final List<Task> history = manager.getHistory();
        assertEquals(1, history.size(), "История пустая.");
        assertEquals(savedTask, history.getFirst(), "Задачи не совпадают.");

        assertEquals(savedTask.getStatus(), history.getFirst().getStatus(), "Статусы не совпадают.");
        assertEquals(savedTask.getDescription(), history.getFirst().getDescription(), "Описания не совпадают.");
        assertEquals(savedTask.getTitle(), history.getFirst().getTitle(), "Названия не совпадают.");
    }

    @Test
    void testRemoveNodeFromHistory() throws ManagerSaveException {
        HistoryManager historyManager = manager.getHistoryManager();
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
        manager.addTask(task1);
        manager.getTaskById(task1.getId());
        List<Task> history0 = historyManager.getHistory();
        assertNotEquals(history0.size(), 0, "История пустая.");

        historyManager.removeFromHistory(task1.getId());
        List<Task> history = historyManager.getHistory();
        assertEquals(history.size(), 0, "История не пустая.");
    }

    @Test
    void testAddNodeFromHistory() throws ManagerSaveException {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
        manager.addTask(task1);
        manager.getTaskById(task1.getId());
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
    }
}
