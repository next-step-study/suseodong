package webserver;

import static util.HttpRequestHeaderUtils.*;
import static util.HttpRequestUtils.*;
import static util.IOUtils.*;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.file.Files;
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
            if (parsedHeader.containsKey("Content-Length")) {
                reqBody = readData(br, Integer.parseInt(parsedHeader.get("Content-Length")));
            }
            log.info("----HTTP Request end----");

            DataOutputStream dos = new DataOutputStream(out);
            byte[] resBody;
            if (!reqUrl.isEmpty()) {
                File file = new File("./webapp" + reqUrl);
                if (file.exists()) {
                    resBody = Files.readAllBytes(new File("./webapp" + reqUrl).toPath());
                } else {
                    if (reqUrl.startsWith("/user/create")) {
                        if (reqMethod.equals("GET")) {
                            int startPosition = reqUrl.indexOf("?");
                            String queryParams = reqUrl.substring(startPosition + 1);
                            Map<String, String> parsedQueryString = parseQueryString(queryParams);
                            signUp(parsedQueryString);
                        } else if (reqMethod.equals("POST")) {
                            Map<String, String> parsedRequestBody = parseQueryString(reqBody.toString());
                            signUp(parsedRequestBody);
                        }
                        log.info("SignUp with " + reqMethod);
                        response302Header(dos, "/index.html");
                        return;
                    } else {
                        resBody = "Invalid RequestUrl".getBytes();
                    }
                }
            } else {
                resBody = "Empty RequestUrl".getBytes();
            }

            response200Header(dos, resBody.length);
            responseBody(dos, resBody);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private User signUp(Map<String, String> parsedQueryString) {
        String userId = parsedQueryString.get("userId");
        String password = parsedQueryString.get("password");
        String name = parsedQueryString.get("name");
        String email = parsedQueryString.get("email");

        User user = new User(userId, password, name, email);
        log.info("user id: " + user.getUserId() + ", user password: " + user.getPassword() + ", user name: " + user.getName() + ", user email: " + user.getEmail());
        return user;
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
