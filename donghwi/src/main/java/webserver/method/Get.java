package webserver.method;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import webserver.response.ResponseBody;
import webserver.response.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Get {

    public static void issue0(DataOutputStream dos) {
        byte[] body = "Hello World".getBytes();
        response(dos, body);
    }

    public static void issue1(DataOutputStream dos, List<String[]> header) throws IOException {
        byte[] body = Files.readAllBytes(Path.of("./webapp" + header.get(0)[1]));
        response(dos, body);
    }

    public static void issue2(DataOutputStream dos, List<String[]> header) {
        String userId = null, password = null, name = null;
        String[] loginInfo = header.get(0)[1].substring(13).split("&");
        for(String info : loginInfo) {
            String[] userInfo = info.split("=");
            switch (userInfo[0]) {
                case "userId" -> userId = userInfo[1];
                case "password" -> password = userInfo[1];
                case "name" -> name = userInfo[1];
            }
        }
        User user = new User(userId, password, name, null);
        DataBase.addUser(user);
    }

    public static void issue5(DataOutputStream dos, List<String[]> header) throws IOException {
        boolean isLogin = !DataBase.findAll().isEmpty();
        if(isLogin) {
            byte[] successBody = Files.readAllBytes(Path.of("./webapp/index.html"));
            responseCookie(dos, successBody, "logined=true");
        } else {
            byte[] failBody = Files.readAllBytes(Path.of("./webapp/user/login_failed.html"));
            responseCookie(dos, failBody, "logined=false");
        }
    }

    private static void response(DataOutputStream dos, byte[] body) {
        ResponseHeader.response200Header(dos, body.length);
        ResponseBody.responseBody(dos, body);
    }

    private static void responseCookie(DataOutputStream dos, byte[] body, String cookie) {

        ResponseBody.responseBody(dos, body);
    }

    public static void issue7(DataOutputStream dos, List<String[]> header) throws IOException {
        String url = header.get(0)[1];
        byte[] body = Files.readAllBytes(Path.of("./webapp" + url));
        ResponseHeader.response200CssHeader(dos, body.length);
        ResponseBody.responseBody(dos, body);
    }
}
