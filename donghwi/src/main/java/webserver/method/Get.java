package webserver.method;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.response.ResponseBody;
import webserver.response.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class Get {

    public void handleGetRequest(DataOutputStream dos, String url, List<String[]> headers) throws IOException {
        if(url.startsWith("favicon")) {
            //어떻게 구현할까?
        }

        if ("/".equals(url)) {
            response(dos, "Hello World".getBytes());
        } else if (url.endsWith(".html")) {
            response(dos, Files.readAllBytes(Path.of("./webapp" + url)));
        } else if (url.startsWith("/user/create")) {
            handleUserCreate(dos, url);
        } else if (url.startsWith("/user/login")) {
            handleUserLogin(dos);
        } else if (url.endsWith(".css")) {
            responseCss(dos, Files.readAllBytes(Path.of("./webapp" + url)));
        }
    }

    private void handleFavicon(DataOutputStream dos) throws IOException {
        String[] faviconFiles = {"/favicon.ico", "/favicon.png", "/favicon.svg"};

        for (String file : faviconFiles) {
            Path path = Path.of("./webapp" + file);
            if (Files.exists(path)) {
                byte[] body = Files.readAllBytes(path);
                ResponseHeader.responseFavicon(dos, body, ResponseHeader.getContentType(file));
                return;
            }
        }
        log.warn("Favicon not found in ./webapp directory.");
    }

    private void handleUserCreate(DataOutputStream dos, String url) {
        String[] params = url.substring(13).split("&");
        User user = new User();

        for(String param : params) {
            String[] keyValue = param.split("=");
            switch (keyValue[0]) {
                case "userId" -> user.setUserId(keyValue[1]);
                case "password" -> user.setPassword(keyValue[1]);
                case "name" -> user.setName(keyValue[1]);
            }
        }
        DataBase.addUser(user);
    }

    private void handleUserLogin(DataOutputStream dos) throws IOException {
        boolean isLogin = !DataBase.findAll().isEmpty();
        byte[] body = isLogin ? Files.readAllBytes(Path.of("./webapp/index.html"))
                : Files.readAllBytes(Path.of("./webapp/user/login_failed.html"));
        ResponseBody.responseBody(dos, body);
    }

    private void response(DataOutputStream dos, byte[] body) throws IOException {
        ResponseHeader.response200Header(dos, body.length);
        ResponseBody.responseBody(dos, body);
    }

    private void responseCss(DataOutputStream dos, byte[] body) throws IOException {
        ResponseHeader.response200CssHeader(dos, body.length);
        ResponseBody.responseBody(dos, body);
    }
}
