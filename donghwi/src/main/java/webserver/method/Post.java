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
}
