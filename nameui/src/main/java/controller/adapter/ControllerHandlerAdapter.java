package controller.adapter;

import controller.Controller;
import http.request.Request;
import http.response.HttpResponse;
import http.response.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class ControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public void handle(Request request, Response response, Object handler) throws IOException {
        Controller controller = (Controller) handler;
        controller.process(request, response);
    }
}
