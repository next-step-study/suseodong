package http.response;

import constants.HttpStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ResponseData {
    HttpStatus httpStatus;
    String contentType;
    byte[] body;
    String cookie;
    String location;
}


