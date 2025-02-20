package controller.adapter;

import http.request.Request;
import http.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public interface HandlerAdapter {
    boolean supports(Object handler);
    HttpResponse handle(Request httpRequest, DataOutputStream dos, Object handler) throws IOException;
}
