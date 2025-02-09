package webserver;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUrlUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            log.info("----HTTP Request start----");
            String line = br.readLine();
            String requestUrl = HttpRequestUrlUtils.getRequestUrl(line);
            while (!Strings.isNullOrEmpty(line)) {
                log.info(line);
                line = br.readLine();
            }
            log.info("----HTTP Request end----");

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body;
            if (requestUrl != null) {
                File file = new File("./webapp" + requestUrl);
                if (file.exists()) {
                    body = Files.readAllBytes(new File("./webapp" + requestUrl).toPath());
                } else body = "Hello World".getBytes();
            } else {
                body = "Hello World".getBytes();
            }

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
