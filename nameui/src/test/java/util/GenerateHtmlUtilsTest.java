package util;

import junit.framework.TestCase;
import model.User;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;

public class GenerateHtmlUtilsTest extends TestCase {

    public void testGenerateUserTableHtml() {
        //given
        List<User> users = new ArrayList<>();
        users.add(new User("userId1", "ddd", "user1", "ddd@naver.com"));
        users.add(new User("userId2", "dddd", "user2", "dddd@naver.com"));

        //when
        String userTableHtml = GenerateHtmlUtils.generateUserTableHtml(users);

        //then
        assertThat(userTableHtml, containsString("user1"));
        assertThat(userTableHtml, containsString("user2"));
    }
}