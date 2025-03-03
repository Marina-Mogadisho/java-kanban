package http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;
import util.DurationAdapter;
import util.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
    }

    protected TaskManager getManager() {
        return manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        sendNotFound(exchange);
    }

    public String getBodyRequest(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    protected void sendResponseCode(int cod, HttpExchange httpExchange, String responseTask) throws IOException {
        switch (cod) {
            case 200:
                sendText(httpExchange, responseTask);
                break;
            case 201:
                sendWithoutText(httpExchange);
                break;
            case 404:
                sendNotFound(httpExchange);
                break;
            case 406:
                sendHasInteractions(httpExchange);
                break;
            case 500:
                sendInternalServerError(httpExchange);
                break;
            default:
                sendInternalServerError(httpExchange);
        }
    }


    /**
     * КОД 200
     * Метод для отправки общего ответа в случае успеха
     * если сервер корректно выполнил запрос и вернул данные
     **/
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    /**
     * КОД 201
     * Метод для отправки общего ответа в случае успеха
     * если сервер корректно выполнил запрос, но данные возвращать не требуется. Например, при создании задачи
     **/
    protected void sendWithoutText(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(201, 0);
        h.getResponseBody().write("".getBytes());
        h.close();
    }


    /**
     * КОД ОШИБКИ 404 (Not Found)
     * Метод для отправки ответа в случае, если объект не был найден
     */
    protected void sendNotFound(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, 0);
        h.getResponseBody().write("".getBytes());
        h.close();
    }

    /**
     * КОД ОШИБКИ 406 (Not Acceptable)
     * Метод для отправки ответа,
     * если при создании или обновлении задача пересекается с уже существующими.
     */
    protected void sendHasInteractions(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, 0);
        h.getResponseBody().write("".getBytes());
        h.close();
    }

    /**
     * КОД ОШИБКИ 500
     * Метод для отправки ответа,
     * если при обработке запроса возникла ошибка, например при сохранении данных менеджера в файл.
     */
    protected void sendInternalServerError(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(500, 0);
        h.getResponseBody().write("".getBytes());
        h.close();
    }

    /**
     * Метод преобразования строки Json в задачи их родных типов (Task, SubTask и Эпик)
     * с учетом установленного формата через адаптер
     */
    public static <T extends Task> T jsonToTask(String jsonTask, Class<T> clazz) {
        Gson gson = createJson();
        return gson.fromJson(jsonTask, clazz);
    }

    public static <T extends Task> List<T> jsonToList(String jsonTask, Type listType) {
        Gson gson = createJson();
        return gson.fromJson(jsonTask, listType);
    }

    /**
     * Метод преобразования всех типов задач в строку Json
     * с учетом установленного формата через адаптер
     */

    private static Gson createJson() {
        GsonBuilder gsonBuilder = new GsonBuilder(); // регламентируем новые параметры
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

    public static <T> String taskToJson(T task, Class<T> clazz) {
        Gson gson = createJson();
        return gson.toJson(task, clazz);
    }

    public static <T> String listToJson(List<T> tasks) {
        Gson gson = createJson();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        return gson.toJson(tasks, listType);
    }

}


