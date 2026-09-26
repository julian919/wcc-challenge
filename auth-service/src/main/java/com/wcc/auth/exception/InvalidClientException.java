package com.wcc.auth.exception;

import com.wcc.commons.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidClientException extends ApiException {

    public InvalidClientException() {
        super(HttpStatus.UNAUTHORIZED, "INVALID_CLIENT", "Invalid client credentials");
    }
}
