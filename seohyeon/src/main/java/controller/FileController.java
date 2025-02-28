package controller;

import java.io.IOException;
import model.http.HttpRequest;
import model.http.HttpResponse;

public class FileController extends AbstractController {

    @Override
    void doGet(HttpRequest request, HttpResponse response) throws IOException {
        String reqUrl = request.getPath();
        response.forward("./webapp" + reqUrl);
    }
}
