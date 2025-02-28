package controller;

import java.io.IOException;
import java.util.Map;
import model.http.HttpRequest;
import model.http.HttpResponse;

public class Controllers {

    private static Map<String, Controller> controllers = Map.of(
            "/", new DefaultController(),
            "/user/login", new LogInController(),
            "/user/create", new CreateUserController(),
            "/user/list.html", new ListUserController());

    public static void run(HttpRequest request, HttpResponse response, String url) throws IOException {
        Controller controller = controllers.get(url);

        if (controller == null) {
            if (url.endsWith(".html") || url.endsWith(".css")) {
                controller = new FileController();
            }
        }

        if (controller != null) {
            controller.service(request, response);
        }
    }
}
