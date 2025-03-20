package model.http;

import db.HttpSessions;
import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    private String sessionId;

    private Map<String, Object> sessionValues = new HashMap<String, Object>();

    public HttpSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getId() {
        return this.sessionId;
    }

    public void setAttribute(String name, Object value) {
        this.sessionValues.put(name, value);
    }

    public Object getAttribute(String name) {
        return this.sessionValues.get(name);
    }

    public void removeAttribute(String name) {
        this.sessionValues.remove(name);
    }

    public void invalidate() {
        HttpSessions.remove(this.sessionId);
    }
}
