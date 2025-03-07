package controller;

import java.io.IOException;
import model.http.HttpMethod;
import model.http.HttpRequest;
import model.http.HttpResponse;

public abstract class AbstractController implements Controller{

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        HttpMethod httpMethod = request.getMethod();
        if (httpMethod.equals(HttpMethod.GET)) doGet(request, response);
        else if (httpMethod.equals(HttpMethod.POST)) doPost(request, response);
    }

    void doGet(HttpRequest request, HttpResponse response) throws IOException {};

    void doPost(HttpRequest request, HttpResponse response) throws IOException {};
}
