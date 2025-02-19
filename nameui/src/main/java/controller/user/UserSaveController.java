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

public class UserSaveController implements Controller {
    @Override
    public Response process(Request request, DataOutputStream dos) throws IOException {
        Map<String, String> userString = request.getBody();

        if (!userString.isEmpty()) {
            User user = new User(userString.get("userId"), userString.get("password"), userString.get("name"), userString.get("email"));
            DataBase.addUser(user);

            byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());

            return new Response(HttpStatus.HTTP_STATUS_302, "html", body, null, "/index.html");
        }
        /**
         * FIXME : null 반환하지 않도록 바꿔보기
         */
        return null; // 이건 해결하기
    }
}
