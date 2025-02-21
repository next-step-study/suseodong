package util.http;

import java.util.Collections;
import java.util.Map;
import util.HttpRequestUtils;

public class ReqBody {

    private Map<String, String> body;

    public ReqBody(String queryString) {
        Map<String, String> parsedQueryString = HttpRequestUtils.parseQueryString(queryString);
        this.body = Collections.unmodifiableMap(parsedQueryString);
    }

    public String getValue(String key) {
        if (body.containsKey(key)) {
            return this.body.get(key);
        }
        return "";
    }
}
