package webserver;

import static util.HttpRequestHeaderUtils.*;
import static util.HttpRequestUtils.*;
import static util.IOUtils.*;

import com.google.common.base.Strings;
import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        boolean isLogined = false;

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            String reqMethod = parseRequestMethod(line);
            String reqUrl = parseRequestUrl(line);
            String reqHeader = "";
            String reqBody = "";

            log.info("----HTTP Request start----");
            log.info(line);
            while (!Strings.isNullOrEmpty(line)) {
                line = br.readLine();
                log.info(line);
                reqHeader += line + "\n";
            }

            Map<String, String> parsedHeader = parseRequestHeader(reqHeader);
            if (parsedHeader.containsKey("Cookie")) {
                Map<String, String> cookies = parseCookies(parsedHeader.get("Cookie"));
                if (Boolean.parseBoolean(cookies.get("logined"))) isLogined = true;
            }
            if (parsedHeader.containsKey("Content-Length")) {
                reqBody = readData(br, Integer.parseInt(parsedHeader.get("Content-Length")));
            }
            log.info("----HTTP Request end----");

            DataOutputStream dos = new DataOutputStream(out);
            byte[] resBody;
            if (!reqUrl.equals("/")) {
                File file = new File("./webapp" + reqUrl);
                if (file.exists()) {
                    if (reqUrl.startsWith("/user/list")) {
                        if (isLogined) {
                            StringBuilder users = createUserList();
                            resBody = users.toString().getBytes();
                        } else {
                            response302Header(dos, "/user/login.html");
                            return;
                        }
                    } else {
                        resBody = Files.readAllBytes(new File("./webapp" + reqUrl).toPath());
                    }
                } else {
                    if (reqUrl.startsWith("/user/create")) {
                        if (reqMethod.equals("GET")) {
                            int startPosition = reqUrl.indexOf("?");
                            String queryParams = URLDecoder.decode(reqUrl.substring(startPosition + 1), "UTF-8");
                            Map<String, String> parsedQueryString = parseQueryString(queryParams);
                            signUp(parsedQueryString);
                        } else if (reqMethod.equals("POST")) {
                            Map<String, String> parsedRequestBody = parseQueryString(URLDecoder.decode(reqBody, "UTF-8"));
                            signUp(parsedRequestBody);
                        }
                        log.info("SignUp with " + reqMethod);
                        response302Header(dos, "/index.html");
                        return;
                    } else if (reqUrl.startsWith("/user/login")) {
                        Map<String, String> parsedRequestBody = parseQueryString(reqBody);
                        boolean isSuccess = signIn(parsedRequestBody);
                        if (isSuccess) {
                            response302HeaderWithCookie(dos, "/index.html", "logined=true");
                            return;
                        }
                        response302Header(dos, "/user/login_failed.html");
                        return;
                    } else {
                        resBody = "Invalid RequestUrl".getBytes();
                    }
                }
            } else {
                resBody = "Hello World".getBytes();
            }

            response200Header(dos, resBody.length);
            responseBody(dos, resBody);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void signUp(Map<String, String> parsedUserInfo) {
        String userId = parsedUserInfo.get("userId");
        String password = parsedUserInfo.get("password");
        String name = parsedUserInfo.get("name");
        String email = parsedUserInfo.get("email");

        User user = new User(userId, password, name, email);
        log.info(user.toString());
        DataBase.addUser(user);
    }

    private boolean signIn(Map<String, String> parsedUserInfo) {
        String userId = parsedUserInfo.get("userId");
        String password = parsedUserInfo.get("password");

        User userInDb = DataBase.findUserById(userId);
        return userInDb != null && userInDb.getPassword().equals(password);
    }

    private StringBuilder createUserList() {
        StringBuilder userList = new StringBuilder();
        userList.append("<table class=\"table table-hover\">\n<thead>\n<tr>\n")
                .append("<th>사용자 아이디</th> <th>이름</th> <th>이메일</th>\n</tr>\n</thead>\n<tbody>\n");

        Collection<User> users = DataBase.findAll();
        int i = 0;
        for (User user : users) {
            i++;
            userList.append("<tr>\n" + "<th scope=\"row\">").append(i)
                    .append("</th><td>").append(user.getUserId()).append("</td><td>").append(user.getName())
                    .append("</td><td>").append(user.getEmail()).append("</td>\n</tr>\n");
        }

        userList.append("</tbody>\n"
                + "</table>");
        return userList;
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

    private void response302Header(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String redirectUrl, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
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
