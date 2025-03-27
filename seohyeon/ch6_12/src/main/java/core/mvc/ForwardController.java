package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {

    private String forwardUrl;

    public ForwardController(String forwardUrl) {
        this.forwardUrl = forwardUrl;
        if (forwardUrl == null) throw new NullPointerException("forwardUrl이 null입니다.");
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.forwardUrl;
    }
}
