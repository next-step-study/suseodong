package next.web;

import core.db.DataBase;
import next.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user/login")
public class UserLoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("userId");
        String password = req.getParameter("password");

        User findUser = DataBase.findUserById(id);

        if (findUser == null || !findUser.getPassword().equals(password)) {
            resp.sendRedirect("/user/login_failed.jsp");
            return;
        }

        // 로그인 성공 시
        HttpSession session = req.getSession();
        session.setAttribute("user", findUser);

        resp.sendRedirect("/index.jsp");
    }
}
