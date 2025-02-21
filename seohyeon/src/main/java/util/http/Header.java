package util.http;

import java.util.Map;

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

    public int getSize() {
        return this.header.size();
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }
}
