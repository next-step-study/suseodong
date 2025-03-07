package controller;

import db.DataBase;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import model.User;
import model.http.HttpRequest;
import model.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class LogInController extends AbstractController{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    void doPost(HttpRequest request, HttpResponse response) throws IOException {
        boolean isSuccess = signIn(request);
        if (isSuccess) {
            response.addHeader("Set-Cookie", "logined=true");
            response.sendRedirect("/index.html");
            return;
        }
        response.addHeader("Set-Cookie", "logined=false");
        response.sendRedirect("/user/login_failed.html");
    }

    private boolean signIn(HttpRequest request) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User userInDb = DataBase.findUserById(userId);
        return userInDb != null && userInDb.getPassword().equals(password);
    }
}
