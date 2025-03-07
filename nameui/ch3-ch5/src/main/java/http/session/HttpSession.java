package http.session;

import http.request.Request;
import http.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSession implements Session {
    String sessionId;
    Map<String, Object> attributes = new HashMap<>();

    public HttpSession(String id) {
        this.sessionId = id;
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public void invalidate() {
        attributes.clear();
    }
}
