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

    public void handlePostRequest(DataOutputStream dos, String url, Map<String, String> bodies) throws IOException {
        if (url.startsWith("/user/create")) {
            createUser(dos, bodies);
        } else if (url.startsWith("/user/login")) {
            loginUser(dos, bodies);
        }
    }

    private void createUser(DataOutputStream dos, Map<String, String> bodies) throws IOException {
        User user = new User(bodies.get("userId"), bodies.get("password"), bodies.get("name"), bodies.get("email"));
        DataBase.addUser(user);
        ResponseHeader.response302Header(dos, "/index.html");
    }

    private void loginUser(DataOutputStream dos, Map<String, String> bodies) throws IOException {
        User user = DataBase.findUserById(bodies.get("userId"));
        if (user != null && user.getPassword().equals(bodies.get("password"))) {
            ResponseHeader.response302LoginSuccessHeader(dos);
        } else {
            ResponseHeader.response302LoginFailHeader(dos);
        }
    }
}
