package http;

/*
Он будет слушать порт $8080$ и принимать запросы.
Это будет основной класс вашего приложения.
 В нём должен находиться метод main, который будет
  запускаться для начала работы с программой.

 */

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.io.IOException;

import http.handler.*;
import managers.Managers;
import managers.TaskManager;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager manager;
    private HttpServer httpServer;

    public HttpTaskServer() {
        /* Создается объект класса FileBackedTaskManager(), в котором данные записываются в файл, а не в память
          FileBackedTaskManager()  наследует у TaskManager (), поэтому у переменной this.manager тип TaskManager
         */
        this.manager = Managers.getDefault();
    }

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
    }

    public boolean start() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {

            stop();
            return false;
        }
        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager));
        httpServer.createContext("/epics", new EpicsHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
        httpServer.createContext("/", new BaseHttpHandler());
        httpServer.start();

        return true;
    }

    public void stop() {
        if (httpServer != null) httpServer.stop(2);
        httpServer = null;

    }

    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer();
        boolean ret = server.start();
        if (ret) {
            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        } else {
            System.err.println("HTTP-сервер не запущен на " + PORT + " порту");
        }
    }
}
