package com.wcc.commons.exception;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public final class ApiProblemDetail {

    private ApiProblemDetail() {
    }

    public static ProblemDetail of(HttpStatus status, String errorCode, String message) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, message);
        problem.setProperty("errorCode", errorCode);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
