package constants;

import lombok.Getter;

@Getter
public enum HttpStatus {
    HTTP_STATUS_200(200, "OK"),
    HTTP_STATUS_302(302, "FOUND"),
    HTTP_STATUS_401(401, "UNAUTHORIZED");

    private final int status;
    private final String message;

    HttpStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
