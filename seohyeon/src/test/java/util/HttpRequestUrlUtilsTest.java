package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HttpRequestUrlUtilsTest {
    @Test
    public void getRequestUrl() {
        String line = "GET /index.html HTTP/1.1";
        String requestUrl = HttpRequestUrlUtils.getRequestUrl(line);
        assertThat(requestUrl, is("/index.html"));
    }
}
