package model.http;

import static model.http.HttpMethod.GET;
import static model.http.HttpMethod.POST;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HttpRequestLineTest {

    private HttpRequestLine line;

    @Test
    public void requestLine_GET() {
        line = new HttpRequestLine("GET /index.html HTTP/1.1");
        assertEquals(line.getMethod(), GET);
        assertEquals(line.getPath(), "/index.html");

        line = new HttpRequestLine("GET /user/create?id=1&name=abc HTTP/1.1");
        Body param = line.getParam();
        assertEquals(line.getMethod(), GET);
        assertEquals(line.getPath(), "/user/create");
        assertEquals(param.getValue("id"), "1");
        assertEquals(param.getValue("name"), "abc");
    }

    @Test
    public void requestLine_POST() {
        line = new HttpRequestLine("POST /index.html HTTP/1.1");
        assertEquals(line.getMethod(), POST);
        assertEquals(line.getPath(), "/index.html");
    }

    @Test
    public void request_invalid() {
        try {
            line = new HttpRequestLine("INVALID / HTTP/1.1");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

}
