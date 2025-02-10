package util.http;

import java.util.Collections;
import java.util.Map;

public class ReqBody {

    private Map<String, String> body;

    public ReqBody(Map<String, String> body) {
        this.body = Collections.unmodifiableMap(body);
    }

    public String getValue(String key) {
        if (body.containsKey(key)) {
            return this.body.get(key);
        }
        return "";
    }

    public int getSize() {
        return this.body.size();
    }
}
