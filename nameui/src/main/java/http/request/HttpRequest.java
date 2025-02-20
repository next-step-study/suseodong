package http.request;

import constants.HttpMethod;
import util.HttpParserUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.HttpParserUtils.parseHeader;

public class HttpRequest implements Request {
    private final Header header;
    private final RequestLine requestLine;
    private final String body;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.requestLine = getRequestLine(br);
        this.header = getHeader(br);
        this.body = getBody(br, requestLine.getMethod(), header.getHeaderValue("Content-Length"));
    }

    public Map<String, String> getCookies() {

        return HttpParserUtils.parseCookies(header.getHeaderValue("Cookie"));
    }

    public String getHeader(String key) {
        return header.getHeaderValue(key);
    }

    public String getParameters(String key) {
        if (requestLine.getMethod().equals(HttpMethod.POST)) { // POST
            return HttpParserUtils.parseQueryString(body).get(key);
        } else { // GET
            return HttpParserUtils.parseQueryString(requestLine.getQuery()).get(key);
        }
    }

    public String getRequestURI() {
        return requestLine.getUri();
    }

    public HttpMethod getRequestMethod() {
        return requestLine.getMethod();
    }

    private RequestLine getRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();

        if (line == null) {
            throw new IOException();
        }

        // http method
        HttpMethod httpMethod;
        if ("POST".equals(line.split(" ")[0])) {
            httpMethod = HttpMethod.POST;
        } else {
            httpMethod = HttpMethod.GET;
        }

        if (line.split(" ")[1].split("\\?").length == 2) {
            return new RequestLine(httpMethod, line.split(" ")[1].split("\\?")[0], URLDecoder.decode(line.split(" ")[1].split("\\?")[1], "UTF-8"));
        }

        return new RequestLine(httpMethod, line.split(" ")[1].split("\\?")[0], "");
    }

    private Header getHeader(BufferedReader br) throws IOException {

        Map<String, String> header = new HashMap<>();

        // 다음 줄 부터는 ': ' 형식이므로 while 문 돌리기
        String line = br.readLine();
        while(!"".equals(line)) {
            if (line == null) {
                break;
            }

            String key = parseHeader(line).getKey();
            String value = parseHeader(line).getValue();

            header.put(key, value);
            line = br.readLine();
        }
        return new Header(header);
    }

    private String getBody(BufferedReader br, HttpMethod httpMethod, String contentLength) throws IOException {
        boolean isPost = httpMethod.equals(HttpMethod.POST);
        if (isPost) {
            return URLDecoder.decode(IOUtils.readData(br, Integer.parseInt(contentLength)), "UTF-8");
        }
        return null;
    }
}
