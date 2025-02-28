package model.http;

import static util.HttpRequestUrlUtils.parseRequestHeader;
import static util.IOUtils.readData;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class HttpRequest {

    private HttpMethod method;

    private String path;

    private Header header;

    private Body body;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        setFirstLine(br.readLine());
        String rawHeader = getHeaderLines(br);
        header = parseRequestHeader(rawHeader);
        setBody(br);
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public String getHeader(String key) {
        return this.header.getValue(key);
    }

    public String getParameter(String key) {
        return body.getValue(key);
    }

    private void setFirstLine(String firstLine) throws IOException {
        log.info(firstLine);
        String[] tokens = firstLine.split(" ");

        method = HttpMethod.valueOf(tokens[0]);
        if (tokens[1].contains("?")) {
            path = URLDecoder.decode(tokens[1].substring(0, tokens[1].indexOf("?")), "UTF-8");
            body = new Body(URLDecoder.decode(tokens[1].substring(tokens[1].indexOf("?") + 1), "UTF-8"));
        } else {
            path = tokens[1];
        }
    }

    private static String getHeaderLines(BufferedReader br) throws IOException {
        String line = br.readLine();
        String rawHeader = "";

        while (!Strings.isNullOrEmpty(line)) {
            log.info(line);
            rawHeader += line + "\n";
            line = br.readLine();
        }

        return rawHeader;
    }

    private void setBody(BufferedReader br) throws IOException {
        if (header.exists("Content-Length")) {
            String reqBody = readData(br, Integer.parseInt(header.getValue("Content-Length")));
            body = new Body(URLDecoder.decode(reqBody, "UTF-8"));
        }
    }
}
