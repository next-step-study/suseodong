package http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpHeadersTest {

    @Test
    public void add() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Connection: keep-alive");
        assertEquals("keep-alive", headers.getHeader("Connection"));
    }
}