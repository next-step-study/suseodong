package webserver;

import java.io.*;
import java.net.Socket;
import controller.FrontController;
import http.request.HttpRequest;
import http.request.Request;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestHandler extends Thread {
//    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    public static final String BASE_URL = "webapp";

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            // http 요청 처리
            Request request = new HttpRequest(in);

            // FrontController 에게 요청 전달
            FrontController frontController = new FrontController();
            frontController.service(request, new DataOutputStream(out));

        }
        catch (RuntimeException | IOException e) {
            log.error(e.getMessage());
        }
    }




}
