package com.training.user.application.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String clientMessage;

    public ApiException(ErrorCode errorCode, HttpStatus httpStatus, String clientMessage) {
        super(clientMessage);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.clientMessage = clientMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getClientMessage() {
        return clientMessage;
    }
}
