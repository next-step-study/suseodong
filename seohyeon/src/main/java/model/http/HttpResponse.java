package model.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class HttpResponse {

    private String status;

    private Header header;

    private Body body;

    private final DataOutputStream dos;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
        this.header = new Header(new HashMap<>());
    }

    public void addHeader(String key, String value) {
        this.header.add(key, value);
    }

    public void forward(String filePath) {
        try {
            byte[] resBody = Files.readAllBytes(new File(filePath).toPath());

            if (filePath.endsWith(".css")) {
                header.add("Content-Type", "text/css");
            } else if (filePath.endsWith(".html")) {
                header.add("Content-Type", "text/html;charset=utf-8");
            } else if (filePath.endsWith(".js")) {
                header.add("Content-Type", "application/javascript");
            }

            response200Header(resBody.length);
            responseBody(resBody);
        } catch (IOException e) {
            log.error("[HttpResponse] " + e.getMessage());
        }
    }

    public void forwardBody(byte[] body) {
        header.add("Content-Type", "text/html;charset=utf-8");
        response200Header(body.length);
        responseBody(body);
    }

    public void sendRedirect(String redirectPath) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + redirectPath + "\r\n");
            dos.writeBytes("Content-Length: 0\r\n");
            processHeader();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error("[HttpResponse] " + e.getMessage());
        }
    }

    private void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            processHeader();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processHeader() {
        try {
            for (String key : header.getKeySet()) {
                dos.writeBytes(key + ": " + header.getValue(key) + "\r\n");
            }
        } catch (IOException e) {
            log.error("[HttpResponse] " + e.getMessage());
        }
    }
}
