package controller.user;

import constants.HttpStatus;
import controller.Controller;
import db.DataBase;
import http.request.Request;
import http.response.Response;
import model.User;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static webserver.RequestHandler.BASE_URL;

public class UserLoginController implements Controller {
    @Override
    public Response process(Request request, DataOutputStream dos) throws IOException {
        Map<String, String> userString = request.getBody();

        User user = DataBase.findUserById(userString.get("userId"));

        if (user != null && user.getPassword().equals(userString.get("password"))) { // 로그인 성공
            byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());

            return new Response(HttpStatus.HTTP_STATUS_302, "html", body, "logined=true", "/");
        }
        else { // 로그인 실패
            byte[] body = Files.readAllBytes(new File(BASE_URL + "/user/login_failed.html").toPath());

            return new Response(HttpStatus.HTTP_STATUS_401, "html", body, "logined=false", "/user/login_failed");
        }
    }
}
