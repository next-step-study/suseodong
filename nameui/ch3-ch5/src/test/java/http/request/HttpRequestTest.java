package http.request;

import constants.HttpMethod;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


public class HttpRequestTest {

    private final String GET_REQUEST = "GET /user/create?userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net HTTP/1.1\nCookie: logined=true; JSessionId=1234\nHost: localhost:8080\nConnection: keep-alive";
    private final String POST_REQUEST = "POST /index.html HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*\nContent-Length: 73\n" +
                                        "\nuserId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net";
    private final String PUT_REQUEST = "PUT /index.html HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*\nContent-Length: 73\n" +
                                        "\nuserId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net";
    private final String PATCH_REQUEST = "PATCH /index.html HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*\nContent-Length: 73\n" +
                                        "\nuserId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net";
    private final String DELETE_REQUEST = "DELETE /user/1 HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*\nContent-Length: 73";

    @Test
    public void GetCookies() throws IOException {
        InputStream in = new ByteArrayInputStream(GET_REQUEST.getBytes());
        Request httpRequest = new HttpRequest(in);

        assertThat(httpRequest.getCookies().get("logined"), is("true"));
        assertThat(httpRequest.getCookies().get("JSessionId"), is("1234"));
        assertThat(httpRequest.getCookies().get("nothing"), nullValue());
    }

    @Test
    public void GetHeader() throws IOException {
        InputStream in = new ByteArrayInputStream(GET_REQUEST.getBytes());
        Request httpRequest = new HttpRequest(in);

        assertThat(httpRequest.getHeader("Host"), is("localhost:8080"));
        assertThat(httpRequest.getHeader("Connection"), is("keep-alive"));
    }

    @Test
    public void getParameters_GET() throws IOException {
        InputStream in_get = new ByteArrayInputStream(GET_REQUEST.getBytes());
        Request httpGetRequest = new HttpRequest(in_get);

        assertThat(httpGetRequest.getParameters("userId"), is("javajigi"));
        assertThat(httpGetRequest.getParameters("password"), is("password"));
        assertThat(httpGetRequest.getParameters("name"), is("JaeSung"));
        assertThat(httpGetRequest.getParameters("email"), is("javajigi@slipp.net"));
    }

    @Test
    public void getParameters_POST() throws IOException {
        InputStream in_post = new ByteArrayInputStream(POST_REQUEST.getBytes());
        Request httpPostRequest = new HttpRequest(in_post);

        assertThat(httpPostRequest.getParameters("userId"), is("javajigi"));
        assertThat(httpPostRequest.getParameters("password"), is("password"));
        assertThat(httpPostRequest.getParameters("name"), is("JaeSung"));
        assertThat(httpPostRequest.getParameters("email"), is("javajigi@slipp.net"));
    }

    @Test
    public void getParameters_PATCH() throws IOException {
        InputStream in = new ByteArrayInputStream(PATCH_REQUEST.getBytes());
        Request request = new HttpRequest(in);

        assertThat(request.getParameters("userId"), is("javajigi"));
        assertThat(request.getParameters("password"), is("password"));
        assertThat(request.getParameters("name"), is("JaeSung"));
        assertThat(request.getParameters("email"), is("javajigi@slipp.net"));
    }

    @Test
    public void getParameters_PUT() throws IOException {
        InputStream in = new ByteArrayInputStream(PUT_REQUEST.getBytes());
        Request request = new HttpRequest(in);

        assertThat(request.getParameters("userId"), is("javajigi"));
        assertThat(request.getParameters("password"), is("password"));
        assertThat(request.getParameters("name"), is("JaeSung"));
        assertThat(request.getParameters("email"), is("javajigi@slipp.net"));
    }

    @Test
    public void getParameters_DELETE() throws IOException {
        InputStream in = new ByteArrayInputStream(DELETE_REQUEST.getBytes());
        Request request = new HttpRequest(in);

        assertThat(request.getParameters("userId"), is("1"));
    }

    @Test
    public void GetRequestURI() throws IOException {
        InputStream in = new ByteArrayInputStream(GET_REQUEST.getBytes());
        Request httpRequest = new HttpRequest(in);

        assertThat(httpRequest.getRequestURI(), is("/user/create"));
    }

    @Test
    public void GetRequestMethod() throws IOException {
        InputStream in = new ByteArrayInputStream(POST_REQUEST.getBytes());
        Request httpRequest = new HttpRequest(in);

        assertThat(httpRequest.getRequestMethod(), is(HttpMethod.POST));
    }
}