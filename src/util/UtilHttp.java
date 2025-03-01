package util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UtilHttp {
    public static ResponseClient send(String type, String UrlRequest, Integer id, String suffix, String body) {
        return send(type, UrlRequest + id + suffix, body);
    }

    public static ResponseClient send(String type, String UrlRequest, Integer id, String body) {
        return send(type, UrlRequest + id, body);
    }

    public static ResponseClient send(String type, String UrlRequest, String body) {
        URI url = URI.create(UrlRequest);
        HttpRequest request = null;
        switch (type) {
            case "POST":
                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .header("Accept", "application/json")
                        .build();
                break;
            case "GET":
                request = HttpRequest.newBuilder()
                        .uri(url)
                        .GET()
                        .version(HttpClient.Version.HTTP_1_1)
                        .header("Accept", "application/json")
                        .build();
                break;
            case "DELETE":
                request = HttpRequest.newBuilder()
                        .uri(url)
                        .DELETE()
                        .version(HttpClient.Version.HTTP_1_1)
                        .header("Accept", "application/json")
                        .build();
                break;
        }
        if (request == null) return new ResponseClient(500, "Error type:" + type);
        //отправляем запрос клиента (request) на сервер и получаем ответ - response

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ResponseClient(response.statusCode(), response.body());
        } catch (Exception e) {
            return new ResponseClient(500, "Error open URL:" + UrlRequest);
        }
    }
}
