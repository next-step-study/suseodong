package model.http;

import java.util.Map;
import util.HttpRequestUtils;

public class HttpCookie {

    private Map<String, String> cookies;

    public HttpCookie(String cookieValues) {
        this.cookies = HttpRequestUtils.parseCookies(cookieValues);
    }

    public String getCookie(String name) {
        return this.cookies.get(name);
    }
}
