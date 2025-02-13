package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.GenerateHtmlUtils;
import util.HttpRequestUtils;

@Slf4j
public class RequestHandler extends Thread {
//    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
//    private HttpRequestUtils httpRequestUtils = new HttpRequestUtils();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            // 헤더 읽어오기
            Map<String, String> headers = HttpRequestUtils.readHeaders(in);
            log.info("headers : \n{}", headers);

            // 헤더에서 첫 번째 라인 요청 URL 추출하기
            String url = headers.get("url");

            // POST, GET 구분
            if ("POST".equals(headers.get("method"))) {
                if ("/user/login".equals(url)) {
                    Map<String, String> userString = HttpRequestUtils.parseQueryString(headers.get("content"));

                    User user = DataBase.findUserById(userString.get("userId"));

                    if (user != null && user.getPassword().equals(userString.get("password"))) { // 로그인 성공
                        byte[] body = Files.readAllBytes(new File("webapp" + "/index.html").toPath());

                        DataOutputStream dos = new DataOutputStream(out);
                        response302HeaderWithCookie(dos, body.length, "/index.html");
                        responseBody(dos, body);
                    }
                    else { // 로그인 실패
                        byte[] body = Files.readAllBytes(new File("webapp" + "/user/login_failed.html").toPath());

                        DataOutputStream dos = new DataOutputStream(out);
                        response401HeaderWithCookie(dos, body.length, "/user/login_failed.html");
                        responseBody(dos, body);
                    }
                }
                else if (url.equals("/user/create")) {
                    Map<String, String> userString = HttpRequestUtils.parseQueryString(headers.get("content"));

                    if (!userString.isEmpty()) {
                        User user = new User(userString.get("userId"), userString.get("password"), userString.get("name"), userString.get("email"));
                        DataBase.addUser(user);

                        byte[] body = Files.readAllBytes(new File("webapp" + "/index.html").toPath());

                        DataOutputStream dos = new DataOutputStream(out);
                        response302Header(dos, body.length, "/index.html");
                        responseBody(dos, body);
                    }
                }
            }
            else if("GET".equals(headers.get("method"))) {
                if("/user/list".equals(url)) {
                    Map<String, String> cookies = HttpRequestUtils.parseCookies(headers.get("Cookie"));
                    boolean isLogined = Boolean.parseBoolean(cookies.get("logined"));

                    if (isLogined) { // 로그인 상태
                        String body = new String(Files.readAllBytes(new File("webapp" + "/user/list.html").toPath()));
                        String userTableHtml = GenerateHtmlUtils.generateUserTableHtml(new ArrayList<>(DataBase.findAll()));

                        String resultStr = body.replace("                    {USER_TABLE}", userTableHtml);

                        // 응답 생성
                        byte[] result = resultStr.getBytes(StandardCharsets.UTF_8);
                        DataOutputStream dos = new DataOutputStream(out);
                        response200Header(dos, result.length);
                        responseBody(dos, result);

                    }
                    else { // 로그아웃 상태
                        byte[] body = Files.readAllBytes(new File("webapp" + "/user/login.html").toPath());

                        DataOutputStream dos = new DataOutputStream(out);
                        response401HeaderWithCookie(dos, body.length, "/user/login.html");
                        responseBody(dos, body);
                    }
                } else {
                    // 요청 URL 에 해당하는 파일을 읽어서 전달
                    byte[] body = Files.readAllBytes(new File("webapp" + url).toPath());

                    DataOutputStream dos = new DataOutputStream(out);
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, int lengthOfBodyContent, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:8080" + url + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, int lengthOfBodyContent, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:8080" + url + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Set-Cookie: logined=true\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response401HeaderWithCookie(DataOutputStream dos, int lengthOfBodyContent, String url) {
        try {
            dos.writeBytes("HTTP/1.1 401 UNAUTHORIZED \r\n");
            dos.writeBytes("Location: http://localhost:8080" + url + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Set-Cookie: logined=false\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
