import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TESTManager {
    TaskManager manager;


    @BeforeEach
    public void init() {
        manager = Managers.getDefault();
    }

    @Test
    void testManager() {
        assertNotNull(manager, "Менеджер не найден.");

        List<Task> tasks = manager.getListAllTasks();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());

        List<Epic> epics = manager.getListAllEpic();
        assertNotNull(epics);
        assertTrue(epics.isEmpty());

        List<Subtask> subtasks = manager.getListAllSubtask();
        assertNotNull(subtasks);
        assertTrue(subtasks.isEmpty());

        List<Task> historyList = manager.getHistory();
        assertNotNull(historyList);
        assertTrue(historyList.isEmpty());
    }


    @Test
    void testAddTask() {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW);
        manager.addTask(task1);
        final Task savedTask = manager.getTaskById(task1.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        List<Task> tasks = manager.getListAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.getFirst(), "Задачи не совпадают.");

    }


    @Test
    void testAddEpic() {
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1_Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1", Status.NEW);
        manager.addSubtask(subtask1_Epic1);
        final Epic savedEpic = manager.getEpicById(epic1.getId());

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(savedEpic, epic1, "Задачи не совпадают.");

        List<Epic> epic = manager.getListAllEpic();
        assertNotNull(epic, "Эпики не возвращаются.");
        assertEquals(1, epic.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epic.getFirst(), "Эпики не совпадают.");
    }


    @Test
    void testAddSubtask() {
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1_Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1", Status.NEW);
        manager.addSubtask(subtask1_Epic1);
        final Subtask savedSubtask = manager.getSubtaskById(subtask1_Epic1.getId());
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask1_Epic1, savedSubtask, "Подзадачи не совпадают.");

        ArrayList<Subtask> subtasks = manager.getListAllSubtask();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1_Epic1, subtasks.getFirst(), "Подзадачи не совпадают.");
    }

    @Test
    void testAddHistory() {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW);
        manager.addTask(task1);
        final Task savedTask = manager.getTaskById(task1.getId());
        final List<Task> history = manager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
        assertEquals(savedTask, history.getFirst(), "Задачи не совпадают.");

        assertEquals(savedTask.getStatus(), history.getFirst().getStatus(), "Статусы не совпадают.");
        assertEquals(savedTask.getDescription(), history.getFirst().getDescription(), "Описания не совпадают.");
        assertEquals(savedTask.getTitle(), history.getFirst().getTitle(), "Названия не совпадают.");
    }


    @Test
    void testTaskNotChange() {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW);
        manager.addTask(task1);
        final Task savedTask = manager.getTaskById(task1.getId());
        assertEquals(task1.getTitle(), savedTask.getTitle(), "Названия не совпадают.");
        assertEquals(task1.getDescription(), savedTask.getDescription(), "Описания не совпадают.");
        assertEquals(task1.getStatus(), savedTask.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void testUpdate() {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW);
        assertTrue(manager.addTask(task1), "Задача не найдена.");
        Task task2 = new Task("Task new_2", "Description task new_2", Status.DONE);
        task2.setId(task1.getId());
        assertTrue(manager.updateTask(task2), "Задача не обновлена.");
    }

    @Test
    void testRemoveByIdTask() {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW);
        manager.addTask(task1);
        assertTrue(manager.removeByIdTask(task1.getId()), "Задача не удалена.");
    }

}