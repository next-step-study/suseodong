package controller.adapter;

import http.request.Request;
import http.response.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public interface HandlerAdapter {
    boolean supports(Object handler);
    Response handle(Request httpRequest, DataOutputStream dos, Object handler) throws IOException;
}
