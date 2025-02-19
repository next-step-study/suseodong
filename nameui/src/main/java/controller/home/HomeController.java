package controller.home;

import constants.HttpStatus;
import controller.Controller;
import http.request.Request;
import http.response.Response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static webserver.RequestHandler.BASE_URL;

public class HomeController implements Controller {
    @Override
    public Response process(Request request, DataOutputStream dos) throws IOException {
        byte[] body = Files.readAllBytes(new File(BASE_URL + "/index.html").toPath());

        return new Response(HttpStatus.HTTP_STATUS_200, "html", body);
    }
}
