import managers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TESTManager {
    TaskManager manager;


    @BeforeEach
    public void init() throws IOException {
        //Создает пустой файл в каталоге временных файлов по умолчанию
        File file = File.createTempFile("filecfg", ".txt");
        String newFile = file.getAbsolutePath();  // Возвращает строку абсолютного пути этого абстрактного пути.
        Managers.setFileNameForSave(newFile);
        manager = Managers.getDefault();
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
    }

    @Test
    void testManager() throws ManagerSaveException {
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
/*
    @Test
    void testSave() throws IOException {
        HistoryManager historyManager = manager.getHistoryManager();

        // Создает пустой файл в каталоге временных файлов по умолчанию
        File file = File.createTempFile("filecfg", ".txt");
        String newFile = file.getAbsolutePath();  // Возвращает строку абсолютного пути этого абстрактного пути.
        //todo
        //newFile ="FILE$TEST.txt";
        FileBackedTaskManager fileTaskManager = new FileBackedTaskManager(historyManager, newFile);
        fileTaskManager.save();  // возникнет пустой файл

        boolean ex = file.exists();  //Проверяет, существует ли файл или каталог, обозначенный этим абстрактным путем.
        assertTrue(ex, "Файл не существует");

         /*
        сохранение и загрузку пустого файла;
        сохранение нескольких задач;
        загрузку нескольких задач.
         */
    /*
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
        fileTaskManager.addTask(task1);
        Epic epic1 = new Epic("tasks.Epic 1", "Description epic 1");
        fileTaskManager.addEpic(epic1);

        FileBackedTaskManager testFileTaskManager = new FileBackedTaskManager(historyManager, newFile);
        testFileTaskManager.load();

        List<Task> taskList = testFileTaskManager.getListAllTasks();
        int taskListSize = taskList.size();
        assertEquals(1, taskListSize, "Задачи не добавлены в файл");

        List<Epic> epicList = testFileTaskManager.getListAllEpic();
        int epicListSize = epicList.size();
        assertEquals(1, epicListSize, "Эпик не добавлен в файл");
    }
/*
    @Test
    void testAddTask() throws ManagerSaveException {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
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
    void testAddHistory() throws ManagerSaveException {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
        manager.addTask(task1);
        final Task savedTask = manager.getTaskById(task1.getId());
        final List<Task> history = manager.getHistory();
        assertEquals(1, history.size(), "История пустая.");
        assertEquals(savedTask, history.getFirst(), "Задачи не совпадают.");

        assertEquals(savedTask.getStatus(), history.getFirst().getStatus(), "Статусы не совпадают.");
        assertEquals(savedTask.getDescription(), history.getFirst().getDescription(), "Описания не совпадают.");
        assertEquals(savedTask.getTitle(), history.getFirst().getTitle(), "Названия не совпадают.");
    }


    @Test
    void testTaskNotChange() throws ManagerSaveException {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
        manager.addTask(task1);
        final Task savedTask = manager.getTaskById(task1.getId());
        assertEquals(task1.getTitle(), savedTask.getTitle(), "Названия не совпадают.");
        assertEquals(task1.getDescription(), savedTask.getDescription(), "Описания не совпадают.");
        assertEquals(task1.getStatus(), savedTask.getStatus(), "Статусы не совпадают.");
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
    void testRemoveNodeFromHistory() throws ManagerSaveException {
        HistoryManager historyManager = manager.getHistoryManager();
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW, "10", "10:50 14.02.2025");
        manager.addTask(task1);
        manager.getTaskById(task1.getId());
        //historyManager.addHistory(task1);
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
        //historyManager.addHistory(task1);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
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
    */
}