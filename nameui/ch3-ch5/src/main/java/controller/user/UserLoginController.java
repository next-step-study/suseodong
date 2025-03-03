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

import static webserver.RequestHandler.BASE_URL;

public class UserLoginController implements Controller {
    @Override
    public void process(Request request, Response response) throws IOException {
        if (request.getRequestMethod().equals(HttpMethod.POST)) {
            doPost(request, response);
        }
    }

    private void doPost(Request request, Response response) throws IOException {
        User user = DataBase.findUserById(request.getParameters("userId"));

        if (user != null && user.getPassword().equals(request.getParameters("password"))) { // 로그인 성공
            byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());

            response.setStatus(HttpStatus.HTTP_STATUS_302);
            response.addHeader("Content-Type", "html");
            response.setBody(body);
            response.addCookie("logined", "true");
            response.addHeader("Location", "/");

            response.forward();
        }
        else { // 로그인 실패
            byte[] body = Files.readAllBytes(new File(BASE_URL + "/user/login_failed.html").toPath());

            response.setStatus(HttpStatus.HTTP_STATUS_302);
            response.addHeader("Content-Type", "html");
            response.setBody(body);
            response.addCookie("logined", "false");
            response.addHeader("Location", "/user/login_failed.html");

            response.forward();
        }
    }
}
