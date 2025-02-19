package controller;

import http.request.Request;
import http.response.Response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public interface Controller {
    Response process(Request request, DataOutputStream dos) throws IOException;
}
