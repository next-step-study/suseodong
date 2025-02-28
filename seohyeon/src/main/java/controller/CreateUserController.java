package controller;

import db.DataBase;
import java.io.IOException;
import model.User;
import model.http.HttpRequest;
import model.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class CreateUserController extends AbstractController{

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    void doPost(HttpRequest request, HttpResponse response) throws IOException {
        signUp(request);
        log.info("SignUp with " + request.getMethod());
        response.sendRedirect("/index.html");
    }

    private void signUp(HttpRequest request) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        User user = new User(userId, password, name, email);
        log.info(user.toString());
        DataBase.addUser(user);
    }
}
