package http.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import http.handler.Endpoint.EndpointType;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = new Endpoint("history",
                httpExchange.getRequestURI().getPath(),httpExchange.getRequestMethod());
        String responseTask = "";
        int cod; // код ошибки или успеха
        if (Objects.requireNonNull(endpoint.getType()) == EndpointType.GET) {   // вывести список задач
            List<Task> tasks = getManager().getHistory(); // получили список задач
            responseTask = listToJson(tasks);
            cod = 200;
        } else {
            cod = 500;
        }
        sendResponseCode(cod, httpExchange, responseTask); // выводим код ошибки
    }
}
