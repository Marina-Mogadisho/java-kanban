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
import tasks.Task;
import util.ResponseClient;
import util.UtilHttp;
import util.UtilTime;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PrioritizedHandlerTest {
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
    public void testGetPrioritized() throws IOException {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW,
                UtilTime.stringOfDuration("10"), UtilTime.stringOfLocalTime("10:50 14.02.2025"));
        manager.addTask(task1);
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addEpic(epic1);
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1",
                Status.NEW, UtilTime.stringOfDuration("1"), UtilTime.stringOfLocalTime("09:50 15.02.2025"));
        manager.addSubtask(subtask1Epic1);


        ResponseClient ret2 = UtilHttp.send("GET", "http://localhost:8080/prioritized", null);
        String responseBody = ret2.getBody();
        int cod2 = ret2.getCod();

        List<Task> hh = manager.getPrioritizedTasks();
        List<Task> responsList = BaseHttpHandler.jsonTopPioritized(responseBody);/**/
        assertEquals(hh, responsList, "История просмотров не совпадает.");

        // проверяем код ответа
        assertEquals(200, cod2);
    }
}


