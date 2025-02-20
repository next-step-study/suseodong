package http.response;

import constants.HttpStatus;

import java.io.DataOutputStream;
import java.util.Map;

public interface Response {
    void forward();
    void addCookie(String key, String value);
    void addHeader(String key, String value);
    void setStatus(HttpStatus httpStatus);
    void setBody(byte[] body);
    Map<String, String> getCookie();
    Map<String, String> getHeader();
    HttpStatus getStatus();
}
