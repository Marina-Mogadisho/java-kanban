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

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HistoryHandlerTest {
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
    public void testGetHistory() throws IOException {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW,
                UtilTime.stringOfDuration("10"), UtilTime.stringOfLocalTime("10:50 14.02.2025"));
        manager.addTask(task1);
        manager.getTaskById(task1.getId());

        ResponseClient ret2 = UtilHttp.send("GET", "http://localhost:8080/history", null);
        String responseBody = ret2.getBody();
        int cod2 = ret2.getCod();

        List<Task> hh = manager.getHistory();
        List<Task> responsList = BaseHttpHandler.jsonToListTasks(responseBody);
        assertEquals(hh, responsList, "История просмотров не совпадает.");

        // проверяем код ответа
        assertEquals(200, cod2);
    }
}
