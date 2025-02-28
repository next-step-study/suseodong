package model.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
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

    public void addHeader(String key, String value) throws IOException {
        this.header.addHeader(key, value);
    }

    public void response200Header(String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            processHeader(dos, this.header);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void forward(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] resBody = Files.readAllBytes(file.toPath());

        if (filePath.endsWith(".css")) {
            response200Header("text/css", resBody.length);
        } else if (filePath.endsWith(".html")) {
            response200Header("text/html;charset=utf-8", resBody.length);
        }

        responseBody(resBody);
    }

    public void sendRedirect(String redirectPath) throws IOException {
        dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
        dos.writeBytes("Location: " + redirectPath + "\r\n");
        dos.writeBytes("Content-Length: 0\r\n");
        dos.writeBytes("Content-Type: text/html\r\n");
        processHeader(dos, this.header);
        dos.writeBytes("\r\n");
        dos.flush();
    }

    private void processHeader(DataOutputStream dos, Header header) throws IOException {
        for (Map.Entry<String, String> entry : header.get().entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
    }
}
