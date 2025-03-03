package http.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {

    public SubtasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = new Endpoint("subtasks",
                httpExchange.getRequestURI().getPath(),httpExchange.getRequestMethod());
        String bodyText=getBodyRequest(httpExchange);
        String responseSubTask = "";
        int cod; // код ошибки или успеха
        switch (endpoint.getType()) {
            case GET_ID:    // вывести задачу по ID
                try {
                    Subtask subtaskById = getManager().getSubtaskById(endpoint.getId()); // получили задачу по ID из запроса
                    responseSubTask = taskToJson(subtaskById, Subtask.class); // сериализовали TASK в Gson для передачи ответа
                    cod = 200;
                } catch (Exception e) {
                    cod = 404;
                }
                break;
            case GET:   // вывести список задач
                List<Subtask> subtasks = getManager().getListAllSubtask(); // получили список задач
                responseSubTask = listToJson(subtasks);
                cod = 200;
                break;
            case POST_CREATE:    // создать задачу
                try {
                    Subtask subtaskCreate = jsonToTask(bodyText, Subtask.class);
                    getManager().addSubtask(subtaskCreate);
                    cod = 201;
                } catch (Exception e) {
                    cod = 406;
                }
                break;
            case POST_UPDATE:
                try {
                    Subtask subtaskUpdate = jsonToTask(bodyText, Subtask.class);
                    getManager().updateSubtaskAndEpic(subtaskUpdate); // обновили задачу
                    cod = 201;
                } catch (Exception e) {
                    cod = 406;
                }
                break;
            case DELETE_ID:
                getManager().getSubtaskById(endpoint.getId());  // получили задачу по ID из запроса
                getManager().removeSubtaskById(endpoint.getId()); // удалили задачу
                cod = 200;
                break;
            default:
                cod = 500;
        }
        sendResponseCode(cod, httpExchange, responseSubTask); // выводим код ошибки
    }
}

