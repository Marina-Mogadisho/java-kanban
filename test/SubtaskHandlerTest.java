import http.HttpTaskServer;
import http.handler.BaseHttpHandler;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import util.ResponseClient;
import util.UtilHttp;
import util.UtilTime;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SubtaskHandlerTest {
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
    public void testAddSubtask() throws IOException {
        // создаём эпик и подзадачу
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1",
                Status.NEW, UtilTime.stringOfDuration("1"), UtilTime.stringOfLocalTime("09:50 15.02.2025"));
        // manager.addSubtask(subtask1Epic1);

        // конвертируем подзадачу в JSON
        String taskJson = BaseHttpHandler.taskToJson(subtask1Epic1, Subtask.class);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret2 = UtilHttp.send("POST", "http://localhost:8080/subtasks", taskJson);
        int cod = ret2.getCod();

        assertEquals(201, cod);

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getListAllSubtask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtask 1", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubtasksList() throws IOException {
        // создаём эпик и подзадачу
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1",
                Status.NEW, UtilTime.stringOfDuration("1"), UtilTime.stringOfLocalTime("09:50 15.02.2025"));
        // manager.addSubtask(subtask1Epic1);

        // конвертируем подзадачу в JSON
        String taskJson = BaseHttpHandler.taskToJson(subtask1Epic1, Subtask.class);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret2 = UtilHttp.send("POST", "http://localhost:8080/subtasks", taskJson);
        int cod = ret2.getCod();

        assertEquals(201, cod);

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getListAllSubtask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtask 1", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");

        //----------------Получаем подзадачу-----------------------------------
        ResponseClient ret3 = UtilHttp.send("GET", "http://localhost:8080/subtasks", null);
        String responseBody = ret3.getBody();
        int cod2 = ret3.getCod();

        // проверяем код ответа
        assertEquals(200, cod2);

        List<Subtask> tasksList = manager.getListAllSubtask(); // получили список задач из файла через менеджер
        // получили список задач из тела ответа сервера
        List<Subtask> responsList = BaseHttpHandler.jsonToListSubtasks(responseBody);

        Subtask t1 = tasksList.getFirst();  // первая задача из коллекции тасок из файла
        Subtask t2 = responsList.getFirst();  // первая задача из ответа
        assertEquals(t1, t2);

        assertEquals(tasksList, responsList, "Списки подзадач из ответа сервера и менеджера  не совпадают");
        assertEquals(t1, t2, "Первые подзадачи коллекций из ответа сервера и менеджера  не совпадают");

    }

    @Test
    public void testGetSubtasksForId() throws IOException {
        // создаём эпик и подзадачу
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1",
                Status.NEW, UtilTime.stringOfDuration("1"), UtilTime.stringOfLocalTime("09:50 15.02.2025"));

        // конвертируем подзадачу в JSON
        String taskJson = BaseHttpHandler.taskToJson(subtask1Epic1, Subtask.class);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret2 = UtilHttp.send("POST", "http://localhost:8080/subtasks", taskJson);
        int cod = ret2.getCod();

        assertEquals(201, cod);

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getListAllSubtask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtask 1", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");

        Subtask responsTask = tasksFromManager.getFirst();
        int id1 = responsTask.getId();


        //----------------Получаем подзадачу по id-----------------------------------
        ResponseClient ret3 = UtilHttp.send("GET", "http://localhost:8080/subtasks/", id1, null);
        String responseBody = ret3.getBody();
        int cod2 = ret3.getCod();

        // проверяем код ответа
        assertEquals(200, cod2, "Сервер не отправил нужную задачу по ее id");

        Subtask responsSubtask = BaseHttpHandler.jsonToTask(responseBody, Subtask.class);
        int id = responsSubtask.getId();
        Subtask task = manager.getSubtaskById(id);

        assertEquals(responsTask, task, "Задача из ответа сервера и менеджера  не совпадают");
    }


    @Test
    public void testUpdateSubtask() throws IOException {
        // создаём эпик и подзадачу
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1",
                Status.NEW, UtilTime.stringOfDuration("1"), UtilTime.stringOfLocalTime("09:50 15.02.2025"));

        // конвертируем подзадачу в JSON
        String taskJson = BaseHttpHandler.taskToJson(subtask1Epic1, Subtask.class);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret2 = UtilHttp.send("POST", "http://localhost:8080/subtasks", taskJson);
        int cod = ret2.getCod();

        assertEquals(201, cod);

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getListAllSubtask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtask 1", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");

        //----------------Получаем подзадачу-----------------------------------
        ResponseClient ret3 = UtilHttp.send("GET", "http://localhost:8080/subtasks", null);
        String responseBody = ret3.getBody();
        int cod2 = ret3.getCod();

        // проверяем код ответа
        assertEquals(200, cod2);

        List<Subtask> tasksList = manager.getListAllSubtask(); // получили список задач из файла через менеджер
        // получили список задач из тела ответа сервера
        List<Subtask> responsList = BaseHttpHandler.jsonToListSubtasks(responseBody);
        Subtask responseSubtask = tasksFromManager.getFirst();
        int id1 = responseSubtask.getId();

        Subtask t1 = tasksList.getFirst();  // первая задача из коллекции тасок из файла
        Subtask t2 = responsList.getFirst();  // первая задача из ответа
        assertEquals(t1, t2);

        assertEquals(tasksList, responsList, "Списки подзадач из ответа сервера и менеджера  не совпадают");
        assertEquals(t1, t2, "Первые подзадачи коллекций из ответа сервера и менеджера  не совпадают");

        //--------------Обновляем подзадачу ------------------------------------------

        Subtask subtask1Epic1New = new Subtask(epic1.getId(), "SubtaskNEW", "Description subtask 1",
                Status.NEW, UtilTime.stringOfDuration("1"), UtilTime.stringOfLocalTime("09:50 15.02.2025"));
        subtask1Epic1New.setId(id1);
        String taskJsonNew = BaseHttpHandler.taskToJson(subtask1Epic1New, Subtask.class);  // сериализовали в Json

        ResponseClient ret4 = UtilHttp.send("POST", "http://localhost:8080/subtasks/", id1, taskJsonNew);
        int cod3 = ret4.getCod();
        Subtask task2 = manager.getSubtaskById(id1);

        // проверяем код ответа
        assertEquals(201, cod3);
        assertEquals(subtask1Epic1New, task2);
    }

    @Test
    public void testDeleteSubtasksById() throws IOException {
        // создаём эпик и подзадачу
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1",
                Status.NEW, UtilTime.stringOfDuration("1"), UtilTime.stringOfLocalTime("09:50 15.02.2025"));
        // manager.addSubtask(subtask1Epic1);

        // конвертируем подзадачу в JSON
        String taskJson = BaseHttpHandler.taskToJson(subtask1Epic1, Subtask.class);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret2 = UtilHttp.send("POST", "http://localhost:8080/subtasks", taskJson);
        int cod = ret2.getCod();

        assertEquals(201, cod);

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getListAllSubtask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtask 1", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");

        //----------------Получаем подзадачу-----------------------------------
        ResponseClient ret3 = UtilHttp.send("GET", "http://localhost:8080/subtasks", null);
        String responseBody = ret3.getBody();
        int cod2 = ret3.getCod();

        // проверяем код ответа
        assertEquals(200, cod2);

        List<Subtask> tasksList = manager.getListAllSubtask(); // получили список задач из файла через менеджер
        // получили список задач из тела ответа сервера
        List<Subtask> responsList = BaseHttpHandler.jsonToListSubtasks(responseBody);
        Subtask responseSubtask = tasksFromManager.getFirst();
        int id1 = responseSubtask.getId();

        Subtask t1 = tasksList.getFirst();  // первая задача из коллекции тасок из файла
        Subtask t2 = responsList.getFirst();  // первая задача из ответа
        assertEquals(t1, t2);

        assertEquals(tasksList, responsList, "Списки подзадач из ответа сервера и менеджера  не совпадают");
        assertEquals(t1, t2, "Первые подзадачи коллекций из ответа сервера и менеджера  не совпадают");

        //--------------Удаляем подзадачу ------------------------------------------

        // создаём HTTP-клиент и запрос
        ResponseClient ret4 = UtilHttp.send("DELETE", "http://localhost:8080/subtasks/", id1,
                null);
        int cod3 = ret4.getCod();
        // проверяем код ответа
        assertEquals(200, cod3, "Задача не удалилась.");
    }
}
