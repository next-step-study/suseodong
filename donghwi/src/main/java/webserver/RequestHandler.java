package webserver;

import lombok.extern.slf4j.Slf4j;
import util.IOUtils;
import webserver.method.Get;
import webserver.method.Post;

import java.io.*;
import java.net.Socket;
import java.util.*;

@Slf4j
public class RequestHandler extends Thread {

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        log.info("Total Active Threads: {}", Thread.activeCount());


        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            List<String[]> header = new ArrayList<>();
            Map<String, String> bodies = new HashMap<>();

            int contentLength = 0;

            while(true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.isEmpty()) {
                        break;
                    }

                    String[] tokens = line.split(" ");
                    header.add(tokens);

                    if (tokens[0].startsWith("Content-Length")) {
                        contentLength = Integer.parseInt(tokens[1]);
                    }
                } catch (Exception e) {
                    log.error("error reading request: {}", e.getMessage());
                    break;
                }
            }

            String url = header.get(0)[1];
            if (header.get(0)[0].equals("GET")) {
                //헬로 월드
                if (url.equals("/")) {
                    Get.issue0(dos);
                }
                //요구사항 - 1
                if (url.endsWith(".html")) {
                    Get.issue1(dos, header);
                }
                //요구사항 - 2
                if (url.startsWith("/user/create")) {
                    Get.issue2(dos, header);
                }
                //요구사항 - 5
                if (url.startsWith("/user/login")) {
                    Get.issue5(dos, header);
                }
                //요구사항 - 7
                if (url.endsWith(".css")) {
                    Get.issue7(dos, header);
                }
            }

            if(contentLength > 0) {
                String[] bodyData = IOUtils.readData(reader, contentLength).split("&");
                Arrays.stream(bodyData)
                        .map(arr -> arr.split("="))
                        .filter(arr -> arr.length == 2)
                        .forEach(arr -> bodies.put(arr[0], arr[1]));
            }

            log.debug("Response Body: {}", bodies);

            if(header.get(0)[0].equals("POST")) {
                //요구사항 - 3
                if(url.startsWith("/user/create")) {
                    Post.issue3(dos, bodies, contentLength);
                }
                //로그인
                if(url.startsWith("/user/login")) {
                    Post.userLogin(dos, bodies, contentLength);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
