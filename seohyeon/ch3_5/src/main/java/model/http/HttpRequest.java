package model.http;

import static model.http.HttpMethod.POST;
import static util.HttpRequestUtils.parseHeader;
import static util.IOUtils.readData;

import com.google.common.base.Strings;
import db.HttpSessions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;
import webserver.RequestHandler;

public class HttpRequest {

    private HttpRequestLine httpRequestLine;

    private Header header;

    private Body param;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String firstLine = br.readLine();
            httpRequestLine = new HttpRequestLine(firstLine);

            header = getHeaderLines(br);

            setParam(br);
        } catch (IOException e) {
            log.error("[HttpRequest] " + e.getMessage());
        }
    }

    private static Header getHeaderLines(BufferedReader br) {
        try {
            String line = br.readLine();
            Map<String, String> headerLine = new HashMap<>();

            while (!Strings.isNullOrEmpty(line)) {
                log.info(line);
                Pair pair = parseHeader(line);
                headerLine.put(pair.getKey(), pair.getValue());
                line = br.readLine();
            }

            return new Header(headerLine);
        } catch (IOException e) {
            throw new IllegalArgumentException("[HttpRequest] : " + e.getMessage());
        }
    }

    private void setParam(BufferedReader br) {
        try {
            if (this.getMethod().equals(POST)) {
                String reqBody = readData(br, Integer.parseInt(header.getValue("Content-Length")));
                param = new Body(reqBody);
            } else param = httpRequestLine.getParam();
        } catch (IOException e) {
            log.error("[HttpRequest] " + e.getMessage());
        }
    }

    public HttpMethod getMethod() {
        return this.httpRequestLine.getMethod();
    }

    public String getPath() {
        return this.httpRequestLine.getPath();
    }

    public String getHeader(String key) {
        return this.header.getValue(key);
    }

    public HttpCookie getCookies() { return new HttpCookie(getHeader("Cookie")); }

    public HttpSession getSession() { return HttpSessions.getSession(getCookies().getCookie("JSESSIONID")); }

    public String getParameter(String key) {
        return param.getValue(key);
    }

    public boolean isCookieExist() {
        if (this.header.exists("Cookie")) return true;
        return false;
    }
}
