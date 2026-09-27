package com.wcc.users.user.exception;

import com.wcc.commons.exception.ApiException;
import org.springframework.http.HttpStatus;

public class RegistrationFailedException extends ApiException {

    public RegistrationFailedException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, "REGISTRATION_FAILED", "Registration failed. Please try again later.");
    }
}
