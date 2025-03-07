package model.http;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class HttpRequestLine {

    private HttpMethod method;

    private String path;

    private Body param;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public HttpRequestLine(String requestLine) {
        if (Strings.isNullOrEmpty(requestLine)) throw new IllegalArgumentException("[RequestLine] : " + requestLine + ", 요청 형식이 맞지 않습니다.");
        log.debug("request line : {}", requestLine);
        String[] tokens = requestLine.split(" ");

        if (tokens.length != 3) throw new IllegalArgumentException("[RequestLine] : " + requestLine + ", 요청 형식이 맞지 않습니다.");

        method = HttpMethod.valueOf(tokens[0]);

        int idx = tokens[1].indexOf("?");
        if (idx == -1) {
            path = tokens[1];
            return;
        }
        path = tokens[1].substring(0, idx);
        param = new Body(tokens[1].substring(idx + 1));
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Body getParam() {
        return param;
    }
}
