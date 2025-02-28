package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import webserver.method.Get;
import webserver.method.Post;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RequestHandlerTest {

    @Mock
    private Get getHandler;

    @Mock
    private Post postHandler;

    @InjectMocks
    private RequestHandlerTest requestHandlerTest;

    private DataOutputStream dos;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        outputStream = new ByteArrayOutputStream();
        dos = new DataOutputStream(outputStream);
    }

}