package controller;

import static util.HttpRequestUtils.parseCookies;

import db.DataBase;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import model.User;
import model.http.HttpRequest;
import model.http.HttpResponse;
import model.http.HttpSession;

public class ListUserController extends AbstractController {

    @Override
    void doGet(HttpRequest request, HttpResponse response) throws IOException {
        if (isLogin(request.getSession())) {
            String userListRes = createUserList();
            byte[] resBody = userListRes.getBytes();
            response.forwardBody(resBody);
        } else {
            response.sendRedirect("/user/login.html");
        }
    }

    public boolean isLogin(HttpSession session) {
        Object user = session.getAttribute("user");
        return user != null;
    }

    private String createUserList() {
        StringBuilder userList = new StringBuilder();
        userList.append("<table class=\"table table-hover\">\n<thead>\n<tr>\n")
                .append("<th>사용자 아이디</th> <th>이름</th> <th>이메일</th>\n</tr>\n</thead>\n<tbody>\n");

        Collection<User> users = DataBase.findAll();
        int i = 0;
        for (User user : users) {
            i++;
            userList.append("<tr>\n" + "<th scope=\"row\">").append(i)
                    .append("</th><td>").append(user.getUserId()).append("</td><td>").append(user.getName())
                    .append("</td><td>").append(user.getEmail()).append("</td>\n</tr>\n");
        }

        userList.append("</tbody>\n"
                + "</table>");
        return userList.toString();
    }
}
