package webserver;

import java.io.File;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerLauncher {
    private static final Logger log = LoggerFactory.getLogger(WebServerLauncher.class);
    private static final int DEFAULT_PORT = 8080;
    private static final String WEBAPP_DIR_LOCATION = "webapp/";

    public static void main(String args[]) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("build/tomcat");
        tomcat.setPort(DEFAULT_PORT);
        tomcat.addWebapp("/", new File(WEBAPP_DIR_LOCATION).getAbsolutePath());

        Connector connector = tomcat.getConnector();
        connector.setURIEncoding("UTF-8");

        log.info("Starting webapp from {}", new File(WEBAPP_DIR_LOCATION).getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}