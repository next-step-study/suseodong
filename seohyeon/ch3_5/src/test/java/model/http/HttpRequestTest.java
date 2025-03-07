package model.http;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class HttpRequestTest {

    private HttpRequest httpRequest;

    @Test
    public void request_GET() {
        // Given
        String GET_REQUEST = "GET /login?name=testname&password=testpassword HTTP/1.1\nHost: localhost\n\n";
        InputStream inputStream = new ByteArrayInputStream(GET_REQUEST.getBytes(StandardCharsets.UTF_8));

        // When
        httpRequest = new HttpRequest(inputStream);

        // Then
        assertEquals(httpRequest.getMethod(), HttpMethod.GET);
        assertEquals(httpRequest.getPath(), "/login");
        assertEquals(httpRequest.getParameter("name"), "testname");
        assertEquals(httpRequest.getParameter("password"), "testpassword");
        assertEquals(httpRequest.getHeader("Host"), "localhost");
    }

    @Test
    public void request_POST() {
        // Given
        String POST_REQUEST = "POST /login HTTP/1.1\nHost: localhost\nContent-Length: 35\n\nname=testname&password=testpassword";
        InputStream inputStream = new ByteArrayInputStream(POST_REQUEST.getBytes(StandardCharsets.UTF_8));

        // When
        httpRequest = new HttpRequest(inputStream);

        // Then
        assertEquals(httpRequest.getMethod(), HttpMethod.POST);
        assertEquals(httpRequest.getPath(), "/login");
        assertEquals(httpRequest.getParameter("name"), "testname");
        assertEquals(httpRequest.getParameter("password"), "testpassword");
        assertEquals(httpRequest.getHeader("Host"), "localhost");
        assertEquals(httpRequest.getHeader("Content-Length"), "35");
    }

    @Test
    public void isCookieExist() {
        // Given
        String requestWithCookie = "GET / HTTP/1.1\nHost: localhost\nCookie: logined=true\n\n";
        InputStream inputStream = new ByteArrayInputStream(requestWithCookie.getBytes(StandardCharsets.UTF_8));

        // When
        httpRequest = new HttpRequest(inputStream);

        // Then
        assertTrue(httpRequest.isCookieExist());
    }
}
