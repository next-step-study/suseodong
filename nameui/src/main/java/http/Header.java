package http;

import java.util.Map;

public class Header {

    private final Map<String, String> header;

    public Header(Map<String, String> header) {
        this.header = header;
    }

    public String getHeaderValue(String value) {
        return header.get(value);
    }
}
