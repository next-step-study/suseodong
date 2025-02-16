package http.request;

import constants.HttpMethod;
import lombok.Getter;

@Getter
public class RequestLine {

    private final HttpMethod method;
    private final String url;
    private final String query;

    public RequestLine(HttpMethod method, String url, String query) {
        this.method = method;
        this.url = url;
        this.query = query;
    }
}
