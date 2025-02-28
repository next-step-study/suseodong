package controller;

import java.io.IOException;
import model.http.HttpRequest;
import model.http.HttpResponse;

public class DefaultController extends AbstractController {

    @Override
    void doGet(HttpRequest request, HttpResponse response) throws IOException {
        byte[] resBody = "Hello World".getBytes();

        response.response200Header("text/html;charset=utf-8", resBody.length);
        response.responseBody(resBody);
    }
}
