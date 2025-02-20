package http.response;

import constants.HttpStatus;

import java.util.Map;

public interface Response {
    void forward();
    void addCookie(String key, String value);
    void addHeader(String key, String value);
    void setStatus(HttpStatus httpStatus);
    void setBody(byte[] body);
    String getCookie(String key);
    String getHeader(String key);
    HttpStatus getStatus();
}
