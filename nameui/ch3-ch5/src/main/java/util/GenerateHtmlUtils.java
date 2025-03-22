package util;

import model.User;

import java.util.List;

public class GenerateHtmlUtils {

    public static String generateUserTableHtml(List<User> users) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i<users.size(); i++) {
            sb.append("                    <tr>\n"
                    + "                          <th scope=\"row\">" + (i+1) + "</th> <td>" + users.get(i).getName() + "</td> <td>" + users.get(i).getEmail()
                    + "</td> <td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n                    </tr>\n");
        }

        return sb.toString();
    }
}
