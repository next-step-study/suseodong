package controller.adapter;

import http.request.Request;
import http.response.HttpResponse;
import http.response.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public interface HandlerAdapter {
    boolean supports(Object handler);
    void handle(Request httpRequest, Response response, Object handler) throws IOException;
}
