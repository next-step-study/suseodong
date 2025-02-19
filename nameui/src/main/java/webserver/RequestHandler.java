package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import constants.HttpMethod;
import constants.HttpStatus;
import controller.FrontController;
import controller.etc.CssController;
import controller.etc.HtmlController;
import controller.home.HomeController;
import controller.user.UserListController;
import controller.user.UserLoginController;
import controller.adapter.ControllerHandlerAdapter;
import controller.adapter.HandlerAdapter;
import controller.user.UserSaveController;
import http.request.Header;
import http.request.HttpRequest;
import http.request.Request;
import http.request.RequestLine;
import http.response.Response;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            RequestLine requestLine = HttpRequestUtils.getRequestLine(br);
            Header header = HttpRequestUtils.getHeader(br);
            String content = HttpRequestUtils.getBody(br, requestLine.getMethod(), header.getHeaderValue("Content-Length"));

            Request request = new HttpRequest(header, requestLine, content);

            // FrontController 에게 요청 전달
            FrontController frontController = new FrontController();
            frontController.service(request, new DataOutputStream(out));

        }
        catch (RuntimeException | IOException e) {
            log.error(e.getMessage());
        }
    }




}
