package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import util.http.Header;

public class HttpRequestUrlUtilsTest {

    private static final String firstLine = "GET /index.html HTTP/1.1";
    private static final String headerWithoutFirstLine = "Host: localhost:8080\nConnection: keep-alive\nContent-Length: 59";

    @Test
    public void parseRequestMethod() {
        String requestMethod = HttpRequestUrlUtils.parseRequestMethod(firstLine);
        assertThat(requestMethod, is("GET"));
    }

    @Test
    public void parseRequestMethod_null() {
        String requestMethod = HttpRequestUrlUtils.parseRequestMethod(null);
        assertThat(requestMethod.isEmpty(), is(true));

        requestMethod = HttpRequestUrlUtils.parseRequestMethod("");
        assertThat(requestMethod.isEmpty(), is(true));
    }

    @Test
    public void parseRequestLine() {
        String requestUrl = HttpRequestUrlUtils.parseRequestLine(firstLine);
        assertThat(requestUrl, is("/index.html"));
    }

    @Test
    public void parseRequestLine_null() {
        String requestUrl = HttpRequestUrlUtils.parseRequestLine(null);
        assertThat(requestUrl.isEmpty(), is(true));

        requestUrl = HttpRequestUrlUtils.parseRequestLine("");
        assertThat(requestUrl.isEmpty(), is(true));
    }

    @Test
    public void parseRequestHeader() {
        Header parsedHeader = HttpRequestUrlUtils.parseRequestHeader(headerWithoutFirstLine);
        assertThat(parsedHeader.getSize(), is(3));
        assertThat(parsedHeader.getValue("Host"), is("localhost:8080"));
        assertThat(parsedHeader.getValue("Connection"), is("keep-alive"));
        assertThat(parsedHeader.getValue("Content-Length"), is("59"));
    }

    @Test
    public void parseRequestHeader_null() {
        Header parsedHeader = HttpRequestUrlUtils.parseRequestHeader(null);
        assertThat(parsedHeader.getSize(), is(0));

        parsedHeader = HttpRequestUrlUtils.parseRequestHeader("");
        assertThat(parsedHeader.getSize(), is(0));
    }

    @Test
    public void parseRequestHeader_invalid() {
        Header parsedHeader = HttpRequestUrlUtils.parseRequestHeader("Host: localhost:8080 Connection: keep-alive");
        assertThat(parsedHeader.getSize(), is(0));

        parsedHeader = HttpRequestUrlUtils.parseRequestHeader("Host:localhost:8080\nConnection:keep-alive");
        assertThat(parsedHeader.getSize(), is(0));
    }

}
