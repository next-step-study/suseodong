package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Map;
import org.junit.Test;

public class HttpRequestHeaderUtilsTest {

    private static final String firstLine = "GET /index.html HTTP/1.1";
    private static final String headerWithoutFirstLine = "Host: localhost:8080\nConnection: keep-alive\nContent-Length: 59";

    @Test
    public void parseRequestMethod() {
        String requestMethod = HttpRequestHeaderUtils.parseRequestMethod(firstLine);
        assertThat(requestMethod, is("GET"));
    }

    @Test
    public void parseRequestMethod_null() {
        String requestMethod = HttpRequestHeaderUtils.parseRequestMethod(null);
        assertThat(requestMethod.isEmpty(), is(true));

        requestMethod = HttpRequestHeaderUtils.parseRequestMethod("");
        assertThat(requestMethod.isEmpty(), is(true));
    }

    @Test
    public void parseRequestUrl() {
        String requestUrl = HttpRequestHeaderUtils.parseRequestUrl(firstLine);
        assertThat(requestUrl, is("/index.html"));
    }

    @Test
    public void parseRequestUrl_null() {
        String requestUrl = HttpRequestHeaderUtils.parseRequestUrl(null);
        assertThat(requestUrl.isEmpty(), is(true));

        requestUrl = HttpRequestHeaderUtils.parseRequestUrl("");
        assertThat(requestUrl.isEmpty(), is(true));
    }

    @Test
    public void parseRequestHeader() {
        Map<String, String> parsedHeader = HttpRequestHeaderUtils.parseRequestHeader(headerWithoutFirstLine);
        assertThat(parsedHeader.size(), is(3));
        assertThat(parsedHeader.get("Host"), is("localhost:8080"));
        assertThat(parsedHeader.get("Connection"), is("keep-alive"));
        assertThat(parsedHeader.get("Content-Length"), is("59"));
    }

    @Test
    public void parseRequestHeader_null() {
        Map<String, String> parsedHeader = HttpRequestHeaderUtils.parseRequestHeader(null);
        assertThat(parsedHeader.isEmpty(), is(true));

        parsedHeader = HttpRequestHeaderUtils.parseRequestHeader("");
        assertThat(parsedHeader.isEmpty(), is(true));
    }

    @Test
    public void parseRequestHeader_invalid() {
        Map<String, String> parsedHeader = HttpRequestHeaderUtils.parseRequestHeader("Host: localhost:8080 Connection: keep-alive");
        assertThat(parsedHeader.isEmpty(), is(true));

        parsedHeader = HttpRequestHeaderUtils.parseRequestHeader("Host:localhost:8080\nConnection:keep-alive");
        assertThat(parsedHeader.isEmpty(), is(true));
    }

}
