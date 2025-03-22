package http.filter;

import http.request.Request;
import http.response.Response;
import http.session.Session;
import http.session.SessionManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionFilter implements Filter {
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        // 세션 처리
        String id = request.getCookies().get("JSESSIONID");
        Session session = SessionManager.getSession(id);
        response.addCookie("JSESSIONID", session.getId());
        log.debug("session : {}", session.getId());

        chain.doFilter(request, response);
    }
}
