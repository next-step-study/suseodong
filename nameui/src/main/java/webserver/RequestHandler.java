package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            List<String> headers = HttpRequestUtils.readHeaders(in);

            // 헤더에서 첫 번째 라인 요청 URL 추출하기
            String url = HttpRequestUtils.parseURL(headers.get(0));
            log.info("요청 주소 : {}", url);

            // 요청 URL 에 해당하는 파일을 읽어서 전달
            byte[] body = Files.readAllBytes(new File(url).toPath());

            DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello Nameui".getBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);
//            log.info("s: {}", body);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
