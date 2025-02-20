package http.response;

import constants.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class HttpResponse implements Response {
    private final DataOutputStream dos;
    private HttpStatus httpStatus;
    private byte[] body;
    private Map<String, String> headers;
    private Map<String, String> cookie;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
        this.headers = new HashMap<>();
        this.cookie = new HashMap<>();
    }

    public void forward() {
        try {
            if (httpStatus.equals(HttpStatus.HTTP_STATUS_200)) {
                sendResponse();
            } else { // 302 응답
                sendRedirect();
            }
//            // header 생성
//            dos.writeBytes("HTTP/1.1 " + responseData.getHttpStatus().getStatus() + " " + responseData.getHttpStatus().getMessage() + " \r\n");
//            if (responseData.getLocation() != null) { // 리다이렉트 설정
//                dos.writeBytes("Location: http://localhost:8080" + responseData.getLocation() + "\r\n");
//            }
//            dos.writeBytes("Content-Type: text/" + responseData.getContentType() + ";charset=utf-8\r\n");
//            if (responseData.getCookie() != null) {
//                dos.writeBytes("Set-Cookie: " + responseData.getCookie() + "\r\n");
//            }
//            dos.writeBytes("Content-Length: " + responseData.getBody().length + "\r\n");
//            dos.writeBytes("\r\n");
//
//            // body 생성
//            dos.write(responseData.getBody(), 0, responseData.getBody().length);
//            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendRedirect() throws IOException {
        dos.writeBytes("HTTP/1.1 " + httpStatus.getStatus() + " " + httpStatus.getMessage() + " \r\n");
        writeHeaders();
        writeCookie();
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private void sendResponse() throws IOException {
        dos.writeBytes("HTTP/1.1 " + httpStatus.getStatus() + " " + httpStatus.getMessage() + " \r\n");
        writeHeaders();
        writeCookie();
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private void writeHeaders() throws IOException {
        Set<String> keySet = headers.keySet();

        for (String key : keySet) {
            if ("Content-Type".equals(key)) {
                dos.writeBytes("Content-Type: text/" + headers.get(key) + ";charset=utf-8\r\n");
                continue;
            }
            else if ("Location".equals(key)) {
                dos.writeBytes("Location: http://localhost:8080" + headers.get(key) + "\r\n");
                continue;
            }
            dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
        }
    }

    private void writeCookie() throws IOException {
        if (cookie.isEmpty()) {
            return;
        }
        Set<String> keySet = cookie.keySet();
        String cookies = "Set-Cookie: ";
        for (String key : keySet) {
            cookies += key + "=" + cookie.get(key) + ";";
        }

        dos.writeBytes(cookies + "\r\n");
    }

    @Override
    public void addCookie(String key, String value) {
        cookie.put(key, value);
    }

    @Override
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public void setStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public void setBody(byte[] body) {
        this.body = body;
        addHeader("Content-Length", String.valueOf(body.length));
    }

    @Override
    public Map<String, String> getCookie() {
        return cookie;
    }

    @Override
    public Map<String, String> getHeader() {
        return headers;
    }

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }


}
