package http.response;

import java.io.DataOutputStream;

public interface Response {
    void forward(DataOutputStream dos);
}
