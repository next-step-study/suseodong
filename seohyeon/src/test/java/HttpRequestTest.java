import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.http.HttpMethod.GET;
import static util.http.HttpMethod.POST;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import org.junit.Test;
import util.http.HttpRequest;

public class HttpRequestTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception {
        InputStream in = Files.newInputStream(new File(testDirectory + "Http_GET.txt").toPath());
        HttpRequest request = new HttpRequest(in);

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/user/create"));
        assertThat(request.getHeader("Connection"),is("keep-alive"));
        assertThat(request.getParameter("userId"), is("javajigi"));
    }

    @Test
    public void request_POST() throws Exception {
        InputStream in = Files.newInputStream(new File(testDirectory + "Http_POST.txt").toPath());
        HttpRequest request = new HttpRequest(in);

        assertThat(request.getMethod(), is(POST));
        assertThat(request.getPath(), is("/user/create"));
        assertThat(request.getHeader("Connection"),is("keep-alive"));
        assertThat(request.getParameter("userId"), is("javajigi"));
    }
}
