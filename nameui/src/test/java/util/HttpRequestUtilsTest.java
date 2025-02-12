package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.List;
import java.util.Map;

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
    public void readHeaders() throws IOException {
        String input = "GET /index.html HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        List<String> headers = HttpRequestUtils.readHeaders(in);

        assertThat(headers.isEmpty(), is(Boolean.FALSE));
        assertThat(headers.get(0), is("GET /index.html HTTP/1.1"));
        assertThat(headers.get(1), is("Host: localhost:8080"));
        assertThat(headers.get(2), is("Connection: keep-alive"));
        assertThat(headers.get(3), is("Accept: */*"));
    }

    @Test
    public void parseURL() {
        String input = "GET /user/create?userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net HTTP/1.1";
        String[] url = HttpRequestUtils.parseURL(input);

        assertThat(url[0], is("/user/create"));
        assertThat(url[1], is("userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net"));
    }

    @Test
    public void parseURL_is_not_query_string() {
        String input = "GET /index.html HTTP/1.1";
        String[] url = HttpRequestUtils.parseURL(input);

        assertThat(url.length, is(1));
        assertThat(url[0], is("/index.html"));
    }

    @Test
    public void test() {
        String url = "/user/create?userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net";
        int index = url.indexOf("?");
        log.info("index = {}", index);
        String requestPath = url.substring(0, index);
        String params = url.substring(index+1);
        log.info("requestPath : {}", requestPath);
        log.info("params : {}", params);
    }
}
