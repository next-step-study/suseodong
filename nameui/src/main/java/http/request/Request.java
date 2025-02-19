package http.request;

import constants.HttpMethod;

import java.util.Map;

public interface Request {
    Map<String, String> getCookies();
    String getHeader(String key);
    Map<String, String> getQueries();
    String getRequestURI();
    HttpMethod getRequestMethod();
    Map<String, String> getBody();
}
