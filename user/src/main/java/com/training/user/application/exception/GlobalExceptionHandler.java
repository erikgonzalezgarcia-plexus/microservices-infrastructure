package com.training.user.application.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages = "com.training.user")
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String GENERIC_BAD_REQUEST_MESSAGE = "The request could not be processed";
    private static final String GENERIC_INTERNAL_ERROR_MESSAGE = "An unexpected error occurred";
    private static final String GENERIC_ACCESS_DENIED_MESSAGE = "Access denied";

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException exception, HttpServletRequest request) {
        logHandledException(exception.getHttpStatus(), exception.getErrorCode(), request, exception);
        return buildResponse(exception.getHttpStatus(), exception.getErrorCode(), exception.getClientMessage());
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception exception, HttpServletRequest request) {
        logHandledException(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, request, exception);
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, GENERIC_BAD_REQUEST_MESSAGE);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiErrorResponse> handleSecurityException(SecurityException exception, HttpServletRequest request) {
        logHandledException(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED, request, exception);
        return buildResponse(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED, GENERIC_ACCESS_DENIED_MESSAGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(Exception exception, HttpServletRequest request) {
        logHandledException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR, request, exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR, GENERIC_INTERNAL_ERROR_MESSAGE);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, ErrorCode errorCode, String message) {
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(errorCode.name(), message));
    }

    private void logHandledException(HttpStatus status, ErrorCode errorCode, HttpServletRequest request, Exception exception) {
        logger.error("Handled exception for [{} {}] with status {} and code {}",
                request.getMethod(),
                request.getRequestURI(),
                status.value(),
                errorCode.name(),
                exception);
    }
}
