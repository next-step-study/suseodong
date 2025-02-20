package controller.user;

import constants.HttpMethod;
import constants.HttpStatus;
import controller.Controller;
import db.DataBase;
import http.request.Request;
import http.response.Response;
import http.response.ResponseData;
import util.GenerateHtmlUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;

import static webserver.RequestHandler.BASE_URL;

public class UserListController implements Controller {
    @Override
    public void process(Request request, Response response) throws IOException {
        if (request.getRequestMethod().equals(HttpMethod.GET)) {
            doGet(request, response);
        }
    }

    private void doGet(Request request, Response response) throws IOException {
        Map<String, String> cookies = request.getCookies();
        boolean isLogined = Boolean.parseBoolean(cookies.get("logined"));

        if (isLogined) { // 로그인 상태
            String body = new String(Files.readAllBytes(new File(BASE_URL + "/user/list.html").toPath()));
            String userTableHtml = GenerateHtmlUtils.generateUserTableHtml(new ArrayList<>(DataBase.findAll()));

            String resultStr = body.replace("                    {USER_TABLE}", userTableHtml);

            // 응답 생성
            byte[] result = resultStr.getBytes(StandardCharsets.UTF_8);

            ResponseData responseData = ResponseData.builder().httpStatus(HttpStatus.HTTP_STATUS_200).contentType("html").body(result).build();
            response.forward(responseData);
        }
        else { // 로그아웃 상태
            byte[] body = Files.readAllBytes(new File(BASE_URL + "/user/login.html").toPath());

            ResponseData responseData = ResponseData.builder().httpStatus(HttpStatus.HTTP_STATUS_302)
                    .contentType("html").body(body).cookie("logined=false")
                    .location("/user/login.html").build();
            response.forward(responseData);
        }
    }
}
