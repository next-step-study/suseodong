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

public class UserLoginController implements Controller {
    @Override
    public void process(Request request, Response response) throws IOException {

        User user = DataBase.findUserById(request.getParameters("userId"));

        if (user != null && user.getPassword().equals(request.getParameters("password"))) { // 로그인 성공
            byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());

            ResponseData responseData = ResponseData.builder().httpStatus(HttpStatus.HTTP_STATUS_302)
                    .contentType("html").body(body).cookie("logined=true")
                    .location("/").build();
            response.forward(responseData);
        }
        else { // 로그인 실패
            byte[] body = Files.readAllBytes(new File(BASE_URL + "/user/login_failed.html").toPath());

            ResponseData responseData = ResponseData.builder().httpStatus(HttpStatus.HTTP_STATUS_302)
                    .contentType("html").body(body).cookie("logined=false")
                    .location("/user/login_failed.html").build();
            response.forward(responseData);
        }
    }
}
