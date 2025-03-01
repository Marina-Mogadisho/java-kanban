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


public class EpicHandlerTest {
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
    public void testAddEpic() throws IOException {
        // создаём эпик
        Epic epic1 = new Epic("Epic 1", "Description epic 1");

        // конвертируем подзадачу в JSON
        String taskJson = BaseHttpHandler.epicToJson(epic1);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        ResponseClient ret2 = UtilHttp.send("POST", "http://localhost:8080/epics", taskJson);
        int cod = ret2.getCod();

        assertEquals(201, cod);

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> tasksFromManager = manager.getListAllEpic();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Epic 1", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testGetEpicList() throws IOException {
        // создаём эпик
        Epic epic1 = new Epic("Epic 1", "Description epic 1");

        // конвертируем подзадачу в JSON
        String taskJson = BaseHttpHandler.epicToJson(epic1);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        //ResponseClient ret2 =
        UtilHttp.send("POST", "http://localhost:8080/epics", taskJson);
        //int cod = ret2.getCod();

        //----------------Получаем задачу-----------------------------------
        ResponseClient ret3 = UtilHttp.send("GET", "http://localhost:8080/epics", null);
        String responseBody = ret3.getBody();
        int cod2 = ret3.getCod();

        // проверяем код ответа
        assertEquals(200, cod2);

        List<Epic> tasksList = manager.getListAllEpic(); // получили список задач из файла через менеджер
        // получили список задач из тела ответа сервера
        List<Epic> responsList = BaseHttpHandler.jsonToListEpics(responseBody);

        Epic t1 = tasksList.getFirst();  // первая задача из коллекции тасок из файла
        Epic t2 = responsList.getFirst();  // первая задача из ответа
        assertEquals(t1, t2);
        //int id=t2.getId();

        assertEquals(tasksList, responsList, "Списки задач из ответа сервера и менеджера  не совпадают");
        assertEquals(t1, t2, "Первые задачи коллекций из ответа сервера и менеджера  не совпадают");
    }

    @Test
    public void testGetEpicForId() throws IOException {
        // создаём эпик
        Epic epic1 = new Epic("Epic 1", "Description epic 1");

        // конвертируем подзадачу в JSON
        String taskJson = BaseHttpHandler.epicToJson(epic1);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        //ResponseClient ret2 =
        UtilHttp.send("POST", "http://localhost:8080/epics", taskJson);
        //int cod = ret2.getCod();
        List<Epic> tasksFromManager = manager.getListAllEpic();
        Epic responseTask = tasksFromManager.getFirst();
        int id1 = responseTask.getId();

        //----------------Получаем задачу-----------------------------------
        ResponseClient ret3 = UtilHttp.send("GET", "http://localhost:8080/epics/", id1, null);
        String responseBody = ret3.getBody();
        int cod2 = ret3.getCod();

        // проверяем код ответа
        assertEquals(200, cod2, "Сервер не отправил нужную задачу по ее id");

        Epic responseTask2 = BaseHttpHandler.jsonToEpic(responseBody);
        int id = responseTask2.getId();
        Epic task = manager.getEpicById(id);
        List<Integer> ss = task.getAllSubtask();
        assertNotNull(ss);

        assertEquals(responseTask2, task, "Задача из ответа сервера и менеджера  не совпадают");
    }


    @Test
    public void testGetSubtasksForIdEpic() throws IOException {
        // создаём эпик
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        int id1 = epic1.getId();
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1",
                Status.NEW, UtilTime.stringOfDuration("1"), UtilTime.stringOfLocalTime("09:50 15.02.2025"));
        manager.addSubtask(subtask1Epic1);
        // конвертируем задачу в JSON
        //String taskJson = BaseHttpHandler.epicToJson(epic1);  // сериализовали в Json

        //----------------Получаем подзадачи Эпика по его id -----------------------------------

        ResponseClient ret3 = UtilHttp.send("GET", "http://localhost:8080/epics/", id1, "/subtasks",
                null);
        String responseBody = ret3.getBody();
        int cod2 = ret3.getCod();

        List<Subtask> response = BaseHttpHandler.jsonToListSubtasks(responseBody);
        List<Subtask> ss = manager.getListAllSubtaskForEpicId(id1);

        assertEquals(response, ss, "Списки subtask по id Эпика не совпадают."); //
        assertNotNull(ss);
        assertNotNull(response);

        // проверяем код ответа
        assertEquals(200, cod2, "Сервер не отправил нужную подзадачу по ее id");
    }


    @Test
    public void testUpdateEpic() throws IOException {
        // создаём эпик
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        // конвертируем подзадачу в JSON
        String taskJson = BaseHttpHandler.epicToJson(epic1);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        //ResponseClient ret2 =
        UtilHttp.send("POST", "http://localhost:8080/epics", taskJson);
        //int cod = ret2.getCod();
        List<Epic> tasksFromManager = manager.getListAllEpic();
        Epic responseTask = tasksFromManager.getFirst();
        int id1 = responseTask.getId();
        //----------------Получаем задачу-----------------------------------
        ResponseClient ret3 = UtilHttp.send("GET", "http://localhost:8080/epics/", id1, null);
        String responseBody = ret3.getBody();
        Epic responseTask2 = BaseHttpHandler.jsonToEpic(responseBody);
        int id = responseTask2.getId();
        //Epic task =
        manager.getEpicById(id);

        //--------------Обновляем подзадачу ------------------------------------------

        Epic epicNew = new Epic("Epic 1", "Description epic 1");
        epicNew.setId(id1);
        String taskJsonNew = BaseHttpHandler.epicToJson(epicNew);  // сериализовали в Json

        ResponseClient ret4 = UtilHttp.send("POST", "http://localhost:8080/epics/", id1, taskJsonNew);
        int cod3 = ret4.getCod();
        Epic task2 = manager.getEpicById(id1);

        // проверяем код ответа
        assertEquals(201, cod3);
        assertEquals(epicNew, task2);
    }

    @Test
    public void testDeleteEpicById() throws IOException {
        // создаём эпик
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        // конвертируем подзадачу в JSON
        String taskJson = BaseHttpHandler.epicToJson(epic1);  // сериализовали в Json

        // создаём HTTP-клиент и запрос
        //ResponseClient ret2 =
        UtilHttp.send("POST", "http://localhost:8080/epics", taskJson);
        //int cod = ret2.getCod();
        List<Epic> tasksFromManager = manager.getListAllEpic();
        Epic responseTask = tasksFromManager.getFirst();
        int id1 = responseTask.getId();
        //----------------Получаем задачу-----------------------------------
        ResponseClient ret3 = UtilHttp.send("GET", "http://localhost:8080/epics/", id1, null);
        String responseBody = ret3.getBody();
        Epic responseTask2 = BaseHttpHandler.jsonToEpic(responseBody);
        int id = responseTask2.getId();
        //Epic task =
        manager.getEpicById(id);

        //--------------Удаляем подзадачу ------------------------------------------

        // создаём HTTP-клиент и запрос
        ResponseClient ret4 = UtilHttp.send("DELETE", "http://localhost:8080/epics/", id, null);
        int cod3 = ret4.getCod();
        // проверяем код ответа
        assertEquals(200, cod3, "Задача не удалилась.");
    }
}

