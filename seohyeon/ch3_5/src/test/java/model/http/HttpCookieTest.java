package model.http;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HttpCookieTest {

    @Test
    public void getCookie() {
        HttpCookie cookie = new HttpCookie("a=b;c=d;");
        assertEquals(cookie.getCookie("a"), "b");
        assertEquals(cookie.getCookie("c"), "d");
    }
}
