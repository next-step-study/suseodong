package controller;

import db.DataBase;
import model.User;
import model.http.HttpRequest;
import model.http.HttpResponse;
import model.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class LogInController extends AbstractController{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    void doPost(HttpRequest request, HttpResponse response) {
        User user = signIn(request);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("/index.html");
        }
        response.sendRedirect("/user/login_failed.html");
    }

    private User signIn(HttpRequest request) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User user =  DataBase.findUserById(userId);
        if (user != null && user.login(password)) return user;
        return null;
    }
}
