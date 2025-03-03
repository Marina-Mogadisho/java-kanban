import com.google.gson.GsonBuilder;
import http.handler.BaseHttpHandler;
import managers.IntersectionTaskException;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import util.DurationAdapter;
import util.LocalDateTimeAdapter;
import util.UtilTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class JsonTest {

    @Test
    void testJson2ListTask1() throws IntersectionTaskException {
        String jsonTask = "[{\"title\":\"Task 1\",\"description\":\"Description task 1\",\"status\":\"NEW\",\"duration\":10,\"startTime\":\"10:50 14.02.2025\",\"type\":\"TASK\"}]";
        List<Task> tasks = BaseHttpHandler.jsonToListTasks(jsonTask);
        System.out.println(tasks.getFirst().toString());
    }


    @Test
    void testTask2Json() throws IntersectionTaskException {
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW,
                UtilTime.stringOfDuration("10"), UtilTime.stringOfLocalTime("10:50 14.02.2025"));

        String jsonTaskById = BaseHttpHandler.taskToJson(task1, Task.class);
        System.out.println(jsonTaskById);
//----------------------------------------------------------------------------------------------------------
        Task task2 = BaseHttpHandler.jsonToTask(jsonTaskById, Task.class);
        System.out.println(task2.toString());
//----------------------------------------------------------------------------------------------------------
        String jsonTaskById2 = "{\n" +
                "\t\t\"title\": \"Task 1\",\n" +
                "\t\t\"description\": \"Description task 1\",\n" +
                "\t\t\"id\": 1,\n" +
                "\t\t\"status\": \"NEW\",\n" +
                "\t\t\"duration\": 10,\n" +
                "\t\t\"startTime\": \"10:50 14.02.2025\",\n" +
                "\t\t\"type\": \"TASK\",\n" +
                "\t\t\"lock\": true\n" +
                "\t}";
        //String jsonTaskById2="{\"title\":\"Task 1\",\"description\":\"Description task 1\",\"status\":\"NEW\",\"duration\":10,\"startTime\":\"10:50 14.02.2025\",\"type\":\"TASK\"}";
        Task task3 = BaseHttpHandler.jsonToTask(jsonTaskById2, Task.class);
        System.out.println(task3.toString());

    }

    @Test
    void testJsonSubTask() throws IntersectionTaskException {
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Description subtask 1",
                Status.NEW, UtilTime.stringOfDuration("1"), UtilTime.stringOfLocalTime("09:50 14.02.2025"));

        String jsonSubTask = BaseHttpHandler.taskToJson(subtask1, Subtask.class);
        System.out.println(jsonSubTask);
        Subtask subtask2 = BaseHttpHandler.jsonToTask(jsonSubTask, Subtask.class);
        System.out.println(subtask2.toString());

        String jsonTaskById2 = "{\"idEpic\":1,\"lockEpic\":true,\"title\":\"Subtask 1\",\"description\":\"Description subtask 1\",\"status\":\"NEW\",\"duration\":1,\"startTime\":\"09:50 14.02.2025\",\"type\":\"SUBTASK\",\"lock\":false}";
        Subtask subtask3 = BaseHttpHandler.jsonToTask(jsonTaskById2, Subtask.class);
        System.out.println(subtask3.toString());

    }

    @Test
    void testJsonEpic() throws IntersectionTaskException {
        Epic epic1 = new Epic("tasks.Epic 1", "Description epic 1");

        //Gson gson = new Gson();
        GsonBuilder gsonBuilder = new GsonBuilder(); // регламентируем новые параметры
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());

        //Gson gson = gsonBuilder.create();
        String jsonEpic = BaseHttpHandler.taskToJson(epic1, Epic.class);
        System.out.println(jsonEpic);

        Epic epic2 = BaseHttpHandler.jsonToTask(jsonEpic, Epic.class);
        System.out.println(epic2.toString());

        String jsonEpic2 = "{\"idSubtask\":[],\"endTime\":\"\",\"title\":\"tasks.Epic 1\",\"description\":\"Description epic 1\",\"status\":\"NEW\",\"duration\":\"\",\"startTime\":\"\",\"type\":\"EPIC\",\"lock\":false}\n";
        Epic epic3 = BaseHttpHandler.jsonToTask(jsonEpic2, Epic.class);
        System.out.println(epic3.toString());
    }
}
