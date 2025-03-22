package controller.home;

import constants.HttpMethod;
import constants.HttpStatus;
import controller.Controller;
import http.request.Request;
import http.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static webserver.DispatcherServlet.BASE_URL;

public class HomeController implements Controller {
    @Override
    public void process(Request request, Response response) throws IOException {
        if (request.getRequestMethod().equals(HttpMethod.GET)) {
            doGet(request, response);
        }
    }

    private void doGet(Request request, Response response) throws IOException {
        byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());
        response.setStatus(HttpStatus.HTTP_STATUS_200);
        response.addHeader("Content-Type", "html");
        response.setBody(body);
        response.forward();
    }
}
