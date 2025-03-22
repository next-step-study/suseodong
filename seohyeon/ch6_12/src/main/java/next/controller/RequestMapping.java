package next.controller;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private static Map<String, Controller> controllers = new HashMap<>();
    static {
        controllers.put("/", new HomeController());
        controllers.put("/users/form", new ForwardController("/user/form.jsp"));
        controllers.put("/users/create", new CreateUserController());
        controllers.put("/users/login", new LoginController());
        controllers.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        controllers.put("/users/logout", new LogoutController());
        controllers.put("/users/profile", new ProfileController());
        controllers.put("/users/update", new UpdateUserController());
        controllers.put("/users/updateForm", new UpdateUserFormController());
        controllers.put("/users", new ListUserController());
    }

    public static Controller getController(String url) { return controllers.get(url); }
}
