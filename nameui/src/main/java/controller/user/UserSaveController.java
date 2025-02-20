package controller.user;

import constants.HttpStatus;
import controller.Controller;
import db.DataBase;
import http.request.Request;
import http.response.HttpResponse;
import http.response.Response;
import http.response.ResponseData;
import model.User;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static webserver.RequestHandler.BASE_URL;

public class UserSaveController implements Controller {
    @Override
    public void process(Request request, Response response) throws IOException {

        User user = new User(request.getParameters("userId"), request.getParameters("password"), request.getParameters("name"), request.getParameters("email"));
        DataBase.addUser(user);

        byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());

        ResponseData responseData = ResponseData.builder().httpStatus(HttpStatus.HTTP_STATUS_302)
                .contentType("html").body(body)
                .location("/index.html").build();
        response.forward(responseData);
    }
}
