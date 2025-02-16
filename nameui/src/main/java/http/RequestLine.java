package http;

import lombok.Getter;

@Getter
public class RequestLine {

    private final String method;
    private final String url;
    private final String query;

    public RequestLine(String method, String url, String query) {
        this.method = method;
        this.url = url;
        this.query = query;
    }
}
