package controller.adapter;

import controller.Controller;
import http.request.Request;
import http.response.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class ControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public Response handle(Request httpRequest, DataOutputStream dos, Object handler) throws IOException {
        Controller controller = (Controller) handler;

//        Map<String, String> paramMap = createParamMap(request);

        return controller.process(httpRequest, dos);
    }
}
