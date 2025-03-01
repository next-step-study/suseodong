package http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestParamsTest {

    @Test
    public void add() throws Exception {
        RequestParams params = new RequestParams();
        params.addQueryString("id=1");
        params.addBody("userId=javajigi&password=password");
        assertEquals("1", params.getParameter("id"));
        assertEquals("javajigi", params.getParameter("userId"));
        assertEquals("password", params.getParameter("password"));
    }
}