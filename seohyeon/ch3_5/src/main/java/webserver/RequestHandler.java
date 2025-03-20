package webserver;

import controller.Controller;
import controller.RequestMapping;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import model.http.HttpCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import model.http.HttpRequest;
import model.http.HttpResponse;

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
            log.info("----HTTP Request start----");
            HttpRequest request = new HttpRequest(in);
            String reqUrl = request.getPath();
            log.info("----HTTP Request end----");

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response = new HttpResponse(dos);

            if (isSessionEmpty(request.getCookies())) {
                response.addHeader("Set-Cookie", "JSESSIONID=" + UUID.randomUUID());
            }

            Controller controller = RequestMapping.getController(reqUrl);
            if (controller == null) {
                moveToDefaultPage(reqUrl, response);
            } else {
                controller.service(request, response);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isSessionEmpty(HttpCookie cookies) {
        return cookies.getCookie("JSESSIONID") == null;
    }

    private static void moveToDefaultPage(String reqUrl, HttpResponse response) {
        if (Files.exists(Paths.get("./webapp" + reqUrl))) {
            String defaultPath = "./webapp" + reqUrl;
            response.forward(defaultPath);
        } else {
            byte[] body = "page not exist".getBytes();
            response.forwardBody(body);
        }
    }

}
