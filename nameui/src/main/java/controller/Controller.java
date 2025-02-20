package controller;

import http.request.Request;
import http.response.Response;

import java.io.IOException;

public interface Controller {
    void process(Request request, Response response) throws IOException;
}
