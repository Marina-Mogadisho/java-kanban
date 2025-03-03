
import com.google.gson.reflect.TypeToken;
import http.HttpTaskServer;
import http.handler.BaseHttpHandler;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;
import util.ResponseClient;
import util.UtilHttp;
import util.UtilTime;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TaskHandlerTest {
    TaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
    HttpTaskServer server = new HttpTaskServer(manager);


    @BeforeEach
    void startServer() {
        server.start();
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }


    @Test
    public void testAddTask() throws IOException {
        // создаём задачу
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW,
                UtilTime.stringOfDuration("10"), UtilTime.stringOfLocalTime("10:50 14.02.2025"));
        // конвертируем её в JSON
        String taskJson = BaseHttpHandler.taskToJson(task1, Task.class);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret2 = UtilHttp.send("POST", "http://localhost:8080/tasks", taskJson);
        int cod = ret2.getCod();

        assertEquals(201, cod);

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getListAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Task 1", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTasksList() throws IOException {
        // создаём задачу
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW,
                UtilTime.stringOfDuration("10"), UtilTime.stringOfLocalTime("10:50 14.02.2025"));
        // конвертируем её в JSON
        String taskJson = BaseHttpHandler.taskToJson(task1, Task.class);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret1 = UtilHttp.send("POST", "http://localhost:8080/tasks", taskJson);
        int cod1 = ret1.getCod();
        // проверяем код ответа
        assertEquals(201, cod1);

        //----------------Получаем задачу-----------------------------------
        ResponseClient ret2 = UtilHttp.send("GET", "http://localhost:8080/tasks", null);
        String responseBody = ret2.getBody();
        int cod2 = ret2.getCod();

        // проверяем код ответа
        assertEquals(200, cod2);

        List<Task> tasksList = manager.getListAllTasks(); // получили список задач из файла через менеджер
        // получили список задач из тела ответа сервера
        List<Task> responsList = BaseHttpHandler.jsonToList(responseBody, new TypeToken<List<Task>>() {
        }.getType());

        Task t1 = tasksList.getFirst();  // первая задача из коллекции тасок из файла
        Task t2 = responsList.getFirst();  // первая задача из ответа
        assertEquals(t1, t2);

        assertEquals(tasksList, responsList, "Списки задач из ответа сервера и менеджера  не совпадают");
        assertEquals(t1, t2, "Первые задачи коллекций из ответа сервера и менеджера  не совпадают");
    }

    @Test
    public void testGetTasksForId() throws IOException {
        // создаём задачу
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW,
                UtilTime.stringOfDuration("10"), UtilTime.stringOfLocalTime("10:50 14.02.2025"));
        // конвертируем её в JSON
        String taskJson = BaseHttpHandler.taskToJson(task1, Task.class);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret1 = UtilHttp.send("POST", "http://localhost:8080/tasks", taskJson);
        int cod = ret1.getCod();

        // проверяем код ответа
        assertEquals(201, cod);

        //----------------Получаем задачу-----------------------------------
        ResponseClient ret2 = UtilHttp.send("GET", "http://localhost:8080/tasks/1", null);
        String responseBody = ret2.getBody();
        int cod2 = ret2.getCod();

        // проверяем код ответа
        assertEquals(200, cod2, "Сервер не отправил нужную задачу по ее id");

        Task responsTask = BaseHttpHandler.jsonToTask(responseBody, Task.class);
        int id = responsTask.getId();
        Task task = manager.getTaskById(id);

        assertEquals(responsTask, task, "Задача из ответа сервера и менеджера  не совпадают");
    }


    @Test
    public void testUpdateTask() throws IOException {
        // создаём задачу
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW,
                UtilTime.stringOfDuration("10"), UtilTime.stringOfLocalTime("10:50 14.02.2025"));
        // конвертируем её в JSON
        String taskJson = BaseHttpHandler.taskToJson(task1, Task.class);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret1 = UtilHttp.send("POST", "http://localhost:8080/tasks", taskJson);
        int cod1 = ret1.getCod();

        assertEquals(201, cod1);

        //----------------Получаем задачу-----------------------------------
        ResponseClient ret2 = UtilHttp.send("GET", "http://localhost:8080/tasks/", null);
        String responseBody = ret2.getBody();
        int cod2 = ret2.getCod();

        // проверяем код ответа
        assertEquals(200, cod2, "Сервер не отправил нужную задачу по ее id");
        List<Task> listTask = BaseHttpHandler.jsonToList(responseBody, new TypeToken<List<Task>>() {
        }.getType());
        Task responsTask = listTask.getFirst();
        int id1 = responsTask.getId();
        Task task = manager.getTaskById(id1);

        assertEquals(responsTask, task, "Задача из ответа сервера и менеджера  не совпадают");

        //--------------Обновляем задачу ------------------------------------------

        Task newTask = new Task("TaskUpdate", "Description task 1", Status.NEW,
                UtilTime.stringOfDuration("10"), UtilTime.stringOfLocalTime("10:50 01.04.2025"));
        newTask.setId(id1);
        String taskJsonNew = BaseHttpHandler.taskToJson(newTask, Task.class);  // сериализовали в Json

        ResponseClient ret3 = UtilHttp.send("POST", "http://localhost:8080/tasks/", id1, taskJsonNew);
        int cod3 = ret3.getCod();
        Task task2 = manager.getTaskById(id1);

        // проверяем код ответа
        assertEquals(201, cod3);
        assertEquals(newTask, task2);
    }

    @Test
    public void testDeleteTasksById() throws IOException {
        //--------------- создаём задачу------------------------------
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW,
                UtilTime.stringOfDuration("10"), UtilTime.stringOfLocalTime("10:50 14.02.2025"));
        // конвертируем её в JSON
        String taskJson = BaseHttpHandler.taskToJson(task1, Task.class);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret1 = UtilHttp.send("POST", "http://localhost:8080/tasks", taskJson);
        int cod1 = ret1.getCod();

        assertEquals(201, cod1);

        //----------------Получаем задачу-----------------------------------
        ResponseClient ret2 = UtilHttp.send("GET", "http://localhost:8080/tasks/", null);
        String responseBody = ret2.getBody();
        int cod2 = ret2.getCod();

        // проверяем код ответа
        assertEquals(200, cod2, "Сервер не отправил нужную задачу по ее id");
        List<Task> listTask = BaseHttpHandler.jsonToList(responseBody, new TypeToken<List<Task>>() {
        }.getType());
        Task responsTask = listTask.getFirst();
        int id1 = responsTask.getId();
        Task task = manager.getTaskById(id1);

        assertEquals(responsTask, task, "Задача из ответа сервера и менеджера  не совпадают");

        //--------------Удаляем задачу ------------------------------------------
        // создаём HTTP-клиент и запрос
        ResponseClient ret3 = UtilHttp.send("DELETE", "http://localhost:8080/tasks/", id1, null);
        int cod3 = ret3.getCod();

        // проверяем код ответа
        assertEquals(200, cod3, "Код успеха неверный, задача не удалилась.");
    }
}
