package http.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;

import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = new Endpoint("epics", httpExchange);
        String responseTask = "";
        int cod; // код ошибки или успеха
        switch (endpoint.getType()) {
            case GET_ID:    // вывести задачу по ID
                try {
                    Epic epicsById = getManager().getEpicById(endpoint.getId()); // получили задачу по ID из запроса
                    responseTask = epicToJson(epicsById); // сериализовали TASK в Gson для передачи ответа
                    cod = 200;
                } catch (Exception e) {
                    cod = 404;
                }
                break;
            case GET_SUB_ID: //вывести список подзадач по id Epic
                try {
                    List<Subtask> ss = getManager().getListAllSubtaskForEpicId(endpoint.getId());
                    responseTask = listSubtasksToJson(ss);
                    cod = 200;
                } catch (Exception e) {
                    cod = 404;
                }
                break;
            case GET:   // вывести список задач
                List<Epic> epics = getManager().getListAllEpic(); // получили список задач
                responseTask = listEpicToJson(epics);
                cod = 200;
                break;
            case POST_CREATE:    // создать задачу
                try {
                    Epic epicCreate = jsonToEpic(endpoint.getBodyText());
                    getManager().addEpic(epicCreate);
                    cod = 201;
                } catch (Exception e) {
                    cod = 406;
                }
                break;
            case POST_UPDATE:
                try {
                    Epic epic = jsonToEpic(endpoint.getBodyText());
                    getManager().updateEpic(epic); // обновили задачу
                    cod = 201;
                } catch (Exception e) {
                    cod = 406;
                }
                break;
            case DELETE_ID:
                getManager().getEpicById(endpoint.getId());  // получили задачу по ID из запроса
                getManager().removeSubtaskById(endpoint.getId()); // удалили задачу
                cod = 200;
                break;
            default:
                cod = 500;
        }

        sendResponseCode(cod, httpExchange, responseTask); // выводим код ошибки
    }
}
