package http.request;

import constants.HttpMethod;
import http.session.Session;

import java.util.Map;

public interface Request {
    Map<String, String> getCookies();
    String getHeader(String key);
    String getParameters(String key);
    String getRequestURI();
    HttpMethod getRequestMethod();
    Session getSession();
}
