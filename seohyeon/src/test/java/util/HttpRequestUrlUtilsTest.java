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
    public void parseRequestMethod_null() {
        String requestMethod = HttpRequestUrlUtils.parseRequestMethod(null);
        assertThat(requestMethod.isEmpty(), is(true));

        requestMethod = HttpRequestUrlUtils.parseRequestMethod("");
        assertThat(requestMethod.isEmpty(), is(true));
    }

    @Test
    public void getRequestUrl() {
        String requestUrl = HttpRequestUrlUtils.getRequestUrl(firstLine);
        assertThat(requestUrl, is("/index.html"));
    }

}
