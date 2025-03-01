package http.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = new Endpoint("tasks", httpExchange);

        String responseTask = "";
        int cod; // код ошибки или успеха
        switch (endpoint.getType()) {
            case GET_ID:    // вывести задачу по ID
                try {
                    Task taskById = getManager().getTaskById(endpoint.getId()); // получили задачу по ID из запроса
                    responseTask = taskToJson(taskById); // сериализовали TASK в Gson для передачи ответа
                    cod = 200;
                } catch (Exception e) {
                    cod = 404;
                }
                break;
            case GET:   // вывести список задач
                List<Task> tasks = getManager().getListAllTasks(); // получили список задач
                responseTask = listTasksToJson(tasks);
                cod = 200;
                break;
            case POST_CREATE:    // создать задачу
                try {
                    Task taskCreate = jsonToTask(endpoint.getBodyText());
                    getManager().addTask(taskCreate);
                    cod = 201;
                } catch (Exception e) {
                    cod = 406;
                }
                break;
            case POST_UPDATE:
                try {
                    Task taskUpdate = jsonToTask(endpoint.getBodyText());
                    getManager().updateTask(taskUpdate); // обновили задачу
                    cod = 201;
                } catch (Exception e) {
                    cod = 406;
                }
                break;
            case DELETE_ID:
                getManager().getTaskById(endpoint.getId());  // получили задачу по ID из запроса
                getManager().removeByIdTask(endpoint.getId()); // удалили задачу
                cod = 200;
                break;
            default:
                cod = 500;
        }
        sendResponseCode(cod, httpExchange, responseTask); // выводим код ошибки
    }
}