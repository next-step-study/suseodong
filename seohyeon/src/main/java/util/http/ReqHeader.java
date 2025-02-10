package util.http;

import java.util.Collections;
import java.util.Map;

public class ReqHeader {

    private Map<String, String> header;

    public ReqHeader(Map<String, String> header) {
        this.header = Collections.unmodifiableMap(header);
    }

    public boolean exists(String key) {
        return header.containsKey(key);
    }

    public String getValue(String key) {
        if (header.containsKey(key)) {
            return this.header.get(key);
        }
        return "";
    }

    public int getSize() {
        return this.header.size();
    }
}
