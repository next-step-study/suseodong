package model.http;

import java.util.Map;
import java.util.Set;

public class Header {

    private Map<String, String> header;

    public Header(Map<String, String> header) {
        this.header = header;
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

    public Map<String, String> get() { return this.header; }

    public Set<String> getKeySet() {
        return this.header.keySet();
    }

    public void add(String key, String value) {
        header.put(key, value);
    }
}
