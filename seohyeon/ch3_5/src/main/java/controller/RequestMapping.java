package controller;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private static final Map<String, Controller> controllers = new HashMap<>();
    static {
        controllers.put("/user/login", new LogInController());
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/list", new ListUserController());
    }

    public static Controller getController(String reqUrl) {
        return controllers.get(reqUrl);
    }
}
