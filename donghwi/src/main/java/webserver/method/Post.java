package webserver.method;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.response.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class Post {

    public static void issue3(DataOutputStream dos, Map<String, String> bodies, int contentLength) throws IOException {
        User user = new User(
                bodies.get("userId"),
                bodies.get("password"),
                bodies.get("name"),
                bodies.get("email")
        );
        DataBase.addUser(user);

        ResponseHeader.response302Header(dos, "/index.html");
    }

    public static void userLogin(DataOutputStream dos, Map<String, String> bodies, int contentLength) {
        String userId = bodies.get("userId");
        String password = bodies.get("password");

        User user = DataBase.findUserById(userId);

        if(user != null && user.getPassword().equals(password)) {
            log.info("user logged success : {}", userId);
            ResponseHeader.response302LoginSuccessHeader(dos);
        } else {
            log.info("user logged failed: {}", userId);
            ResponseHeader.response302LoginFailHeader(dos);
        }
    }
}
