package http.request;

import constants.HttpMethod;
import lombok.Getter;

@Getter
public class RequestLine {

    private final HttpMethod method;
    private final String uri;
    private final String query;

    public RequestLine(HttpMethod method, String uri, String query) {
        this.method = method;
        this.uri = uri;
        this.query = query;
    }
}
