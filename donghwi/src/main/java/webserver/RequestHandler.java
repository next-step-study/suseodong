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
    private final Get getHandler;
    private final Post postHandler;

    public RequestHandler(Socket connectionSocket, Get getHandler, Post postHandler) {
        this.connection = connectionSocket;
        this.getHandler = getHandler;
        this.postHandler = postHandler;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        log.info("Total Active Threads: {}", Thread.activeCount());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            List<String[]> headers = parseHeaders(reader);
            Map<String, String> bodies = parseBody(reader, headers);

            if(!headers.isEmpty()) {
                handleRequest(headers, bodies, dos);
            }
        } catch (IOException e) {
            log.error("Handling Request 에러: {}", e.getMessage());
        }
    }

    private List<String[]> parseHeaders(BufferedReader reader) throws IOException {
        List<String[]> headerList = new ArrayList<>();
        String line;

        while( (line = reader.readLine()) != null && !line.isEmpty() ) {
            headerList.add(line.split(" "));
        }

        return headerList;
    }

    private Map<String, String> parseBody(BufferedReader reader, List<String[]> headers) throws IOException {
        Map<String, String> bodyMap = new HashMap<>();
        int contentLength = headers.stream()
                .filter(header -> header[0].equalsIgnoreCase("Content-Length"))
                .mapToInt(header -> Integer.parseInt(header[1]))
                .findFirst()
                .orElse(0);

        if(contentLength > 0) {
            String[] bodyDate = IOUtils.readData(reader, contentLength).split("&");
            Arrays.stream(bodyDate)
                    .map(arr -> arr.split("="))
                    .filter(arr -> arr.length == 2)
                    .forEach(arr -> bodyMap.put(arr[0], arr[1]));
        }

        return bodyMap;
    }

    private void handleRequest(List<String[]> headers, Map<String, String> bodies, DataOutputStream dos) throws IOException {
        String method = headers.get(0)[0];
        String url = headers.get(0)[1];

        switch (method) {
            case "GET" -> getHandler.handleGetRequest(dos, url, headers);
            case "POST" -> postHandler.handlePostRequest(dos, url, bodies);
            default -> log.warn("Unsupported HTTP Method: {}", method);
        }
    }
}
