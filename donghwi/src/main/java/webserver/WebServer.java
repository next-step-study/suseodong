package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.method.Get;
import webserver.method.Post;

@Slf4j
public class WebServer {
    private static final int DEFAULT_PORT = 8080;
    private static final Get getHandler = new Get();
    private static final Post postHandler = new Post();

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection, getHandler, postHandler);
                requestHandler.start();
            }
        }
    }
}
