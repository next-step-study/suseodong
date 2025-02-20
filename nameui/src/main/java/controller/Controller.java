package controller;

import http.request.Request;
import http.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Controller {
    HttpResponse process(Request request, DataOutputStream dos) throws IOException;
}
