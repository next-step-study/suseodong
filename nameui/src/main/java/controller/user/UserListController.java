package controller.user;

import constants.HttpStatus;
import controller.Controller;
import db.DataBase;
import http.request.Request;
import http.response.Response;
import util.GenerateHtmlUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;

import static webserver.RequestHandler.BASE_URL;

public class UserListController implements Controller {
    @Override
    public Response process(Request request, DataOutputStream dos) throws IOException {
        Map<String, String> cookies = request.getCookies();
        boolean isLogined = Boolean.parseBoolean(cookies.get("logined"));

        if (isLogined) { // 로그인 상태
            String body = new String(Files.readAllBytes(new File(BASE_URL + "/user/list.html").toPath()));
            String userTableHtml = GenerateHtmlUtils.generateUserTableHtml(new ArrayList<>(DataBase.findAll()));

            String resultStr = body.replace("                    {USER_TABLE}", userTableHtml);

            // 응답 생성
            byte[] result = resultStr.getBytes(StandardCharsets.UTF_8);
            return new Response(HttpStatus.HTTP_STATUS_200, "html", result);
        }
        else { // 로그아웃 상태
            byte[] body = Files.readAllBytes(new File(BASE_URL + "/user/login.html").toPath());

            return new Response(HttpStatus.HTTP_STATUS_302, "html", body, "logined=false", "/user/login.html");
        }
    }
}
