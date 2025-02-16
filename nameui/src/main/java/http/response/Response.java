package http.response;

import constants.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
public class Response {
    private final HttpStatus httpStatus;
    private final String contentType;
    private final byte[] body;
    private final String cookie;
    private final String location;

    public Response(HttpStatus httpStatus, String contentType, byte[] body) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.body = body;
        this.cookie = null;
        this.location = null;
    }

    Response(HttpStatus httpStatus, String contentType, byte[] body, String cookie) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.body = body;
        this.cookie = cookie;
        this.location = null;

    }

    public Response(HttpStatus httpStatus, String contentType, byte[] body, String cookie, String location) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.body = body;
        this.cookie = cookie;
        this.location = location;
    }

    public void createResponse(DataOutputStream dos) {
        try {
            // header 생성
            dos.writeBytes("HTTP/1.1 " + httpStatus.getStatus() + " " + httpStatus.getMessage() + " \r\n");
            if (location != null) {
                dos.writeBytes("Location: http://localhost:8080" + location + "\r\n");
            }
            dos.writeBytes("Content-Type: text/" + contentType + ";charset=utf-8\r\n");
            if (cookie != null) {
                dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
            }
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");

            // body 생성
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
