package controller.etc;

import constants.HttpStatus;
import controller.Controller;
import http.request.Request;
import http.response.HttpResponse;
import http.response.ResponseData;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static webserver.RequestHandler.BASE_URL;

public class CssController implements Controller {
    @Override
    public HttpResponse process(Request request, DataOutputStream dos) throws IOException {
        String url = request.getRequestURI();
        byte[] css = Files.readAllBytes(new File(BASE_URL + url).toPath());

        ResponseData responseData = ResponseData.builder().httpStatus(HttpStatus.HTTP_STATUS_200).contentType("css").body(css).build();
        return new HttpResponse(responseData);
    }
}
