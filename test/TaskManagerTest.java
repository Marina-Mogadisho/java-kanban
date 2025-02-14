import managers.TaskManager;
import managers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    TaskManager manager;

    @BeforeEach
    public abstract void init() throws IOException;

    @Test
    void testAddTask() {
        Assertions.assertDoesNotThrow(() -> {
            Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
            manager.addTask(task1);
            final Task savedTask = manager.getTaskById(task1.getId());
            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(task1, savedTask, "Задачи не совпадают.");

            List<Task> tasks = manager.getListAllTasks();
            assertNotNull(tasks, "Задачи не возвращаются.");
            assertEquals(1, tasks.size(), "Неверное количество задач.");
            assertEquals(task1, tasks.getFirst(), "Задачи не совпадают.");
        }, "Тест на исключение. Задача не добавилась");
    }

    @Test
    void calcStatusTest() throws ManagerSaveException {

        // ТЕСТ 1. subtask 1", Status.NEW, subtask 2", Status.NEW
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);

        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1", Status.NEW, "1", "09:50 14.02.2025");
        Subtask subtask2Epic1 = new Subtask(epic1.getId(), "Subtask 2", "Description subtask 2", Status.NEW, "2", "10:51 14.02.2025");
        manager.addSubtask(subtask1Epic1);
        manager.addSubtask(subtask2Epic1);
        Status status = epic1.getStatus();
        assertEquals(status, Status.NEW, "ТЕСТ 1. Статус  Epic не равен NEW");

        //ТЕСТ 2. subtask 1", Status.DONE,subtask 2", Status.DONE
        Epic epic2 = new Epic("Epic 2", "Description epic 2");
        manager.addEpic(epic2);

        Subtask subtask1Epic2 = new Subtask(epic2.getId(), "Subtask 1", "Description subtask 1", Status.DONE, "1", "09:50 15.02.2025");
        Subtask subtask2Epic2 = new Subtask(epic2.getId(), "Subtask 2", "Description subtask 2", Status.DONE, "2", "10:51 16.02.2025");
        manager.addSubtask(subtask1Epic2);
        manager.addSubtask(subtask2Epic2);
        Status status2 = epic2.getStatus();
        assertEquals(status2, Status.DONE, "ТЕСТ 2. Статус  Epic должен быть равен DONE");

        //ТЕСТ 3. subtask 1", Status.NEW, subtask 2", Status.DONE
        Epic epic3 = new Epic("Epic 3", "Description epic 3");
        manager.addEpic(epic3);

        Subtask subtask1Epic3 = new Subtask(epic3.getId(), "Subtask 1", "Description subtask 1", Status.NEW, "1", "09:50 17.02.2025");
        Subtask subtask2Epic3 = new Subtask(epic3.getId(), "Subtask 2", "Description subtask 2", Status.DONE, "2", "10:51 18.02.2025");
        manager.addSubtask(subtask1Epic3);
        manager.addSubtask(subtask2Epic3);
        Status status3 = epic3.getStatus();
        assertEquals(status3, Status.IN_PROGRESS, "Статус  Epic должен быть равен NEW");

        //ТЕСТ 4. subtask 1", Status.IN_PROGRESS, subtask 2", Status.IN_PROGRESS
        Epic epic4 = new Epic("Epic4", "Description epic 4");
        manager.addEpic(epic4);

        Subtask subtask1Epic4 = new Subtask(epic4.getId(), "Subtask 1", "Description subtask 1", Status.IN_PROGRESS, "1", "11:50 19.02.2025");
        Subtask subtask2Epic4 = new Subtask(epic4.getId(), "Subtask 2", "Description subtask 2", Status.IN_PROGRESS, "2", "12:51 20.02.2025");
        manager.addSubtask(subtask1Epic4);
        manager.addSubtask(subtask2Epic4);
        Status status4 = epic4.getStatus();
        assertEquals(status4, Status.IN_PROGRESS, "Статус  Epic должен быть равен IN_PROGRESS");
    }

    @Test
    void testAddEpic() throws ManagerSaveException {
        Epic epic1 = new Epic("tasks.Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1", Status.NEW, "1", "09:50 14.02.2025");
        manager.addSubtask(subtask1Epic1);
        final Epic savedEpic = manager.getEpicById(epic1.getId());

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(savedEpic, epic1, "Задачи не совпадают.");

        List<Epic> epic = manager.getListAllEpic();
        assertNotNull(epic, "Эпики не возвращаются.");
        assertEquals(1, epic.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epic.getFirst(), "Эпики не совпадают.");
    }

    @Test
    void testAddSubtask() throws ManagerSaveException {
        Epic epic1 = new Epic("tasks.Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1", Status.NEW, "1", "09:50 14.02.2025");
        manager.addSubtask(subtask1Epic1);
        final Subtask savedSubtask = manager.getSubtaskById(subtask1Epic1.getId());
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask1Epic1, savedSubtask, "Подзадачи не совпадают.");

        ArrayList<Subtask> subtasks = manager.getListAllSubtask();
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1Epic1, subtasks.getFirst(), "Подзадачи не совпадают.");
    }


    @Test
    void testUpdate() throws ManagerSaveException {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
        assertTrue(manager.addTask(task1), "Задача не найдена.");
        Task task2 = new Task("Task 2", "Description task 2", Status.IN_PROGRESS, "15", "11:50 14.02.2025");
        task2.setId(task1.getId());
        assertTrue(manager.updateTask(task2), "Задача не обновлена.");
    }

    @Test
    void testRemoveByIdTask() throws ManagerSaveException {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
        manager.addTask(task1);
        manager.getTaskById(task1.getId());
        assertTrue(manager.removeByIdTask(task1.getId()), "Задача не удалена.");
    }

    @Test
    void testRemoveByIdEpic() throws ManagerSaveException {
        Epic epic1 = new Epic("tasks.Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        manager.getEpicById(epic1.getId());
        assertTrue(manager.removeByIdEpic(epic1.getId()), "Задача не удалена.");
    }

    @Test
    void testRemoveByIdSubtask() throws ManagerSaveException {
        Epic epic1 = new Epic("tasks.Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1", Status.NEW, "1", "09:50 14.02.2025");
        manager.addSubtask(subtask1Epic1);
        manager.getSubtaskById(subtask1Epic1.getId());
        assertTrue(manager.removeSubtaskById(subtask1Epic1.getId()), "Задача не удалена.");
    }

    @Test
    void removeAllSubtasksTest() throws ManagerSaveException {
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1", Status.NEW, "1", "09:50 14.02.2025");
        Subtask subtask2Epic1 = new Subtask(epic1.getId(), "Subtask 2", "Description subtask 2", Status.DONE, "2", "09:51 14.02.2025");
        manager.addSubtask(subtask1Epic1);
        manager.addSubtask(subtask2Epic1);

        assertTrue(manager.removeAllSubtasks(), "Все Subtask  не удалось удалить.");
    }

    @Test
    void testClearID() throws ManagerSaveException {
        Epic epic1 = new Epic("tasks.Epic 1", "Description epic 1");
        manager.addEpic(epic1);

        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1", Status.NEW, "1", "09:50 14.02.2025");
        manager.addSubtask(subtask1Epic1);

        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
        manager.addTask(task1);

        boolean flagTask = manager.removeByIdTask(task1.getId());
        assertTrue(flagTask, "Задача не удалена.");
        assertEquals(task1.getId(), 0, "ID задачи не обнулился при удалении задачи");

        boolean flagEpic = manager.removeByIdEpic(epic1.getId());
        assertTrue(flagEpic, "Эпик не удален.");
        assertEquals(epic1.getId(), 0, "ID эпика не обнулился при удалении эпика");
        assertEquals(subtask1Epic1.getId(), 0, "ID tasks.Subtask не обнулился при удалении эпика");
    }
}
