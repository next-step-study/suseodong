package http.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static Map<String, Session> sessions = new HashMap<>();

    public static Session getSession(String id) {
        Session session = sessions.get(id);
        if (session == null) { // 세션이 비어있다면, 세션 생성
            session = id == null ? new HttpSession(UUID.randomUUID().toString()) : new HttpSession(id);
            sessions.put(session.getId(), session);
            return session;
        }
        return session;
    }

    public static void removeSession(String id) {
        sessions.remove(id);
    }
}
