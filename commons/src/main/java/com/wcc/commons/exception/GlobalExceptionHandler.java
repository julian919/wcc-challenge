package com.wcc.commons.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException e) {
        return ApiProblemDetail.of(e.getStatus(), e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationFailure(AuthenticationException e) {
        return ApiProblemDetail.of(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Missing or invalid token");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException e) {
        return ApiProblemDetail.of(HttpStatus.FORBIDDEN, "ACCESS_DENIED",
                "You don't have permission to do this");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleOtherException(Exception e) {
        // Capturing framework specific errors like url not found
        if (e instanceof ErrorResponse springError) {
            HttpStatus status = HttpStatus.valueOf(springError.getStatusCode().value());
            return ApiProblemDetail.of(status, status.name(), springError.getBody().getDetail());
        }

        log.error("Unexpected error", e);
        return ApiProblemDetail.of(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "Something went wrong. Please try again later.");
    }
}
