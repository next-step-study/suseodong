package http.request;

import constants.HttpMethod;
import util.HttpRequestUtils;

import java.util.Map;

public class HttpRequest implements Request {
    private final Header header;
    private final RequestLine requestLine;
    private final String body;

    public HttpRequest(Header header, RequestLine requestLine, String body) {
        this.header = header;
        this.requestLine = requestLine;
        this.body = body;
    }

    public Map<String, String> getCookies() {

        return HttpRequestUtils.parseCookies(header.getHeaderValue("Cookie"));
    }

    public String getHeader(String key) {
        return header.getHeaderValue(key);
    }

    public Map<String, String> getQueries() {

        return HttpRequestUtils.parseQueryString(requestLine.getQuery());
    }

    public String getRequestURL() {
        return requestLine.getUrl();
    }

    public HttpMethod getRequestMethod() {
        return requestLine.getMethod();
    }

    public Map<String, String> getBody() {

        return HttpRequestUtils.parseQueryString(this.body);
    }
}
