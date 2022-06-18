package edu.muntoclone.exception.handle;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "invalid input value"),
    MISSING_REQUEST_BODY(400, "C002", "missing request body"),
    INTERNAL_SERVER_ERROR(500, "C003", "internal server error"),


    // Authentication
    DUPLICATE_USERNAME(400, "A001", "the email already exists."),
    DUPLICATE_NICKNAME(400, "A002", "the nickname already exists."),

    ACCESS_DENIED(401, "A003", "access denied.")
    ;

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
