package http.response;

import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
public class HttpResponse implements Response {
    private final DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void forward(ResponseData responseData) {
        try {
            // header 생성
            dos.writeBytes("HTTP/1.1 " + responseData.getHttpStatus().getStatus() + " " + responseData.getHttpStatus().getMessage() + " \r\n");
            if (responseData.getLocation() != null) { // 리다이렉트 설정
                dos.writeBytes("Location: http://localhost:8080" + responseData.getLocation() + "\r\n");
            }
            dos.writeBytes("Content-Type: text/" + responseData.getContentType() + ";charset=utf-8\r\n");
            if (responseData.getCookie() != null) {
                dos.writeBytes("Set-Cookie: " + responseData.getCookie() + "\r\n");
            }
            dos.writeBytes("Content-Length: " + responseData.getBody().length + "\r\n");
            dos.writeBytes("\r\n");

            // body 생성
            dos.write(responseData.getBody(), 0, responseData.getBody().length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
