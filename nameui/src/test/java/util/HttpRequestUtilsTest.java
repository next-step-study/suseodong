package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.Map;

import constants.HttpMethod;
import http.request.Header;
import http.request.RequestLine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import util.HttpRequestUtils.Pair;

@Slf4j
public class HttpRequestUtilsTest {
    @Test
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is("password2"));
    }

    @Test
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty(), is(true));
    }

    @Test
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));
    }

    @Test
    public void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertThat(parameters.get("logined"), is("true"));
        assertThat(parameters.get("JSessionId"), is("1234"));
        assertThat(parameters.get("session"), is(nullValue()));
    }

    @Test
    public void getKeyValue() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair, is(new Pair("userId", "javajigi")));
    }

    @Test
    public void getKeyValue_invalid() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair, is(nullValue()));
    }

    @Test
    public void parseHeader() throws Exception {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair, is(new Pair("Content-Length", "59")));
    }

    @Test
    public void getHeader_POST() throws IOException {
        String input = "POST /index.html HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*\nContent-Length: 73\n" +
                        "\nuserId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        RequestLine requestLine = HttpRequestUtils.getRequestLine(br);
        Header header = HttpRequestUtils.getHeader(br);

        assertThat(header, not(nullValue()));
        assertThat(requestLine.getMethod(), is(HttpMethod.POST));
        assertThat(requestLine.getUri(), is("/index.html"));
        assertThat(requestLine.getQuery(), is(""));
        assertThat(header.getHeaderValue("Host"), is("localhost:8080"));
        assertThat(header.getHeaderValue("Connection"), is("keep-alive"));
        assertThat(header.getHeaderValue("Accept"), is("*/*"));
        assertThat(header.getHeaderValue("Content-Length"), is("73"));
    }

    @Test
    public void getHeader_GET() throws IOException {
        String input = "GET /user/create?userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        RequestLine requestLine = HttpRequestUtils.getRequestLine(br);
        Header header = HttpRequestUtils.getHeader(br);

        assertThat(header, not(nullValue()));
        assertThat(requestLine.getMethod(), is(HttpMethod.GET));
        assertThat(requestLine.getUri(), is("/user/create"));
        assertThat(requestLine.getQuery(), is("userId=javajigi&password=password&name=JaeSung&email=javajigi@slipp.net"));
        assertThat(header.getHeaderValue("Host"), is("localhost:8080"));
        assertThat(header.getHeaderValue("Connection"), is("keep-alive"));
    }

    @Test
    public void getBody() throws IOException {
        String input = "POST /index.html HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*\nContent-Length: 73\n" +
                "\nuserId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        RequestLine requestLine = HttpRequestUtils.getRequestLine(br);
        Header header = HttpRequestUtils.getHeader(br);
        String body = HttpRequestUtils.getBody(br, requestLine.getMethod(), header.getHeaderValue("Content-Length"));

        assertThat(body, is("userId=javajigi&password=password&name=JaeSung&email=javajigi@slipp.net"));
    }
}
