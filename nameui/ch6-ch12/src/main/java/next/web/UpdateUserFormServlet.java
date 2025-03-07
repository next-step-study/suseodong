package next.web;

import core.db.DataBase;
import next.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user/update_form.jsp")
public class UpdateUserFormServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 로그인 한 사용자인지 확인
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("/user/login.jsp");
            return;
        }

        // request 에 담아서 jsp 에 전달
        req.setAttribute("user", user);

        RequestDispatcher rd = req.getRequestDispatcher("/user/update_form.jsp");
        rd.forward(req, resp);
    }
}
