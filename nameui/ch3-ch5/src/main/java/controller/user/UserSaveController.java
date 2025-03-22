package controller.user;

import constants.HttpMethod;
import constants.HttpStatus;
import controller.Controller;
import db.DataBase;
import http.request.Request;
import http.response.Response;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static webserver.DispatcherServlet.BASE_URL;

public class UserSaveController implements Controller {
    @Override
    public void process(Request request, Response response) throws IOException {
        if (request.getRequestMethod().equals(HttpMethod.POST)) {
            doPost(request, response);
        }
    }

    private void doPost(Request request, Response response) throws IOException {
        User user = new User(request.getParameters("userId"), request.getParameters("password"), request.getParameters("name"), request.getParameters("email"));
        DataBase.addUser(user);

        byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());

        response.setStatus(HttpStatus.HTTP_STATUS_302);
        response.addHeader("Content-Type", "html");
        response.addHeader("Location", "/index.html");
        response.setBody(body);

        response.forward();
    }
}
