package http.response;

import constants.HttpStatus;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class HttpResponseTest {

    @Test
    public void forward_css() throws UnsupportedEncodingException {
        // given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);
        Response response = new HttpResponse(dos);

        response.setStatus(HttpStatus.HTTP_STATUS_200);
        response.addHeader("Content-Type", "css");
        response.setBody("css".getBytes());

        // when
        response.forward();

        // then
        String result = byteArrayOutputStream.toString("UTF-8");
        assertThat(result, containsString("HTTP/1.1 200 OK"));
        assertThat(result, containsString("Content-Type: text/css;charset=utf-8"));
        assertThat(result, containsString("Content-Length: " + "css".getBytes().length));
        assertThat(result, containsString("\r\n\r\ncss"));

    }

    @Test
    public void forward_html() throws UnsupportedEncodingException {
        // given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        Response response = new HttpResponse(dos);

        response.setStatus(HttpStatus.HTTP_STATUS_200);
        response.addHeader("Content-Type", "html");
        response.setBody("html".getBytes());


        // when
        response.forward();

        // then
        String result = byteArrayOutputStream.toString("UTF-8");
        assertThat(result, containsString("HTTP/1.1 200 OK"));
        assertThat(result, containsString("Content-Type: text/html;charset=utf-8"));
        assertThat(result, containsString("Content-Length: " + "html".getBytes().length));
        assertThat(result, containsString("\r\n\r\nhtml"));
    }

    @Test
    public void forward_js() throws UnsupportedEncodingException {
        // given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        Response response = new HttpResponse(dos);

        response.setStatus(HttpStatus.HTTP_STATUS_200);
        response.addHeader("Content-Type", "javascript");
        response.setBody("javascript".getBytes());

        // when
        response.forward();

        // then
        String result = byteArrayOutputStream.toString("UTF-8");
        assertThat(result, containsString("HTTP/1.1 200 OK"));
        assertThat(result, containsString("Content-Type: text/javascript;charset=utf-8"));
        assertThat(result, containsString("Content-Length: " + "javascript".getBytes().length));
        assertThat(result, containsString("\r\n\r\njavascript"));
    }

    @Test
    public void redirect() throws UnsupportedEncodingException {
        // given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        Response response = new HttpResponse(dos);

        response.setStatus(HttpStatus.HTTP_STATUS_302);
        response.addHeader("Content-Type", "html");
        response.setBody("html".getBytes());
        response.addHeader("Location", "/index.html");
        response.addCookie("logined", "true");


        // when
        response.forward();

        // then
        String result = byteArrayOutputStream.toString("UTF-8");
        assertThat(result, containsString("HTTP/1.1 302 FOUND"));
        assertThat(result, containsString("Content-Type: text/html;charset=utf-8"));
        assertThat(result, containsString("Content-Length: " + "html".getBytes().length));
        assertThat(result, containsString("Location: http://localhost:8080/index.html"));
        assertThat(result, containsString("Set-Cookie: logined=true;"));
        assertThat(result, containsString("\r\n\r\nhtml"));
    }
}