package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HttpRequestUrlUtilsTest {

    private static final String firstLine = "GET /index.html HTTP/1.1";

    @Test
    public void parseRequestMethod() {
        String requestMethod = HttpRequestUrlUtils.parseRequestMethod(firstLine);
        assertThat(requestMethod, is("GET"));
    }

    @Test
    public void parseRequestMethod_invalid() {
        String requestMethod = HttpRequestUrlUtils.parseRequestMethod(null);
        assertThat(requestMethod.isEmpty(), is(true));

        requestMethod = HttpRequestUrlUtils.parseRequestMethod("");
        assertThat(requestMethod.isEmpty(), is(true));
    }

    @Test
    public void parseRequestUrl() {
        String requestUrl = HttpRequestUrlUtils.parseRequestUrl(firstLine);
        assertThat(requestUrl, is("/index.html"));
    }

    @Test
    public void parseRequestUrl_invalid() {
        String requestUrl = HttpRequestUrlUtils.parseRequestUrl(null);
        assertThat(requestUrl.isEmpty(), is(true));

        requestUrl = HttpRequestUrlUtils.parseRequestUrl("");
        assertThat(requestUrl.isEmpty(), is(true));
    }

}
