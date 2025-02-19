package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;

import constants.HttpMethod;
import constants.HttpStatus;
import db.DataBase;
import http.request.Header;
import http.request.HttpRequest;
import http.request.Request;
import http.request.RequestLine;
import http.response.Response;
import lombok.extern.slf4j.Slf4j;
import model.User;
import util.GenerateHtmlUtils;
import util.HttpRequestUtils;

@Slf4j
public class RequestHandler extends Thread {
//    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private static final String BASE_URL = "webapp";

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

            // FrontController 로 넘겨주기(req)


            // 헤더에서 첫 번째 라인 요청 URL 추출하기
            String url = requestLine.getUrl();

            // POST, GET 구분
            if (HttpMethod.POST.equals(requestLine.getMethod())) {
                if ("/user/login".equals(url)) {

                    Map<String, String> userString = request.getBody();

                    User user = DataBase.findUserById(userString.get("userId"));

                    if (user != null && user.getPassword().equals(userString.get("password"))) { // 로그인 성공
                        byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());

                        DataOutputStream dos = new DataOutputStream(out);
                        Response response = new Response(HttpStatus.HTTP_STATUS_302, "html", body, "logined=true", "/");
                        response.createResponse(dos);
                    }
                    else { // 로그인 실패
                        byte[] body = Files.readAllBytes(new File(BASE_URL + "/user/login_failed.html").toPath());

                        DataOutputStream dos = new DataOutputStream(out);
                        Response response = new Response(HttpStatus.HTTP_STATUS_401, "html", body, "logined=false", "/user/login_failed");
                        response.createResponse(dos);
                    }
                }
                else if (url.equals("/user/create")) {
                    Map<String, String> userString = request.getBody();

                    if (!userString.isEmpty()) {
                        User user = new User(userString.get("userId"), userString.get("password"), userString.get("name"), userString.get("email"));
                        DataBase.addUser(user);

                        byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());

                        DataOutputStream dos = new DataOutputStream(out);
                        Response response = new Response(HttpStatus.HTTP_STATUS_302, "html", body, null, "/index.html");
                        response.createResponse(dos);
                    }
                }
            }
            else if(HttpMethod.GET.equals(requestLine.getMethod())) {
                if("/user/list".equals(url)) {
                    Map<String, String> cookies = request.getCookies();
                    boolean isLogined = Boolean.parseBoolean(cookies.get("logined"));

                    if (isLogined) { // 로그인 상태
                        String body = new String(Files.readAllBytes(new File(BASE_URL + "/user/list.html").toPath()));
                        String userTableHtml = GenerateHtmlUtils.generateUserTableHtml(new ArrayList<>(DataBase.findAll()));

                        String resultStr = body.replace("                    {USER_TABLE}", userTableHtml);

                        // 응답 생성
                        byte[] result = resultStr.getBytes(StandardCharsets.UTF_8);
                        DataOutputStream dos = new DataOutputStream(out);
                        Response response = new Response(HttpStatus.HTTP_STATUS_200, "html", result);
                        response.createResponse(dos);
                    }
                    else { // 로그아웃 상태
                        byte[] body = Files.readAllBytes(new File(BASE_URL + "/user/login.html").toPath());

                        DataOutputStream dos = new DataOutputStream(out);
                        Response response = new Response(HttpStatus.HTTP_STATUS_401, "html", body, "logined=false", "/user/login");
                        response.createResponse(dos);
                    }
                }
                else if(url.endsWith(".css")) {
                    byte[] css = Files.readAllBytes(new File(BASE_URL + url).toPath());

                    DataOutputStream dos = new DataOutputStream(out);
                    Response response = new Response(HttpStatus.HTTP_STATUS_200, "css", css);
                    response.createResponse(dos);
                }
                else if("/".equals(url)){
                    // 요청 URL 에 해당하는 파일을 읽어서 전달
                    byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());

                    DataOutputStream dos = new DataOutputStream(out);
                    Response response = new Response(HttpStatus.HTTP_STATUS_200, "html", body);
                    response.createResponse(dos);
                }
                else {
                    // 요청 URL 에 해당하는 파일을 읽어서 전달
                    byte[] body = Files.readAllBytes(new File(BASE_URL + url).toPath());

                    DataOutputStream dos = new DataOutputStream(out);
                    Response response = new Response(HttpStatus.HTTP_STATUS_200, "html", body);
                    response.createResponse(dos);
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }




}
