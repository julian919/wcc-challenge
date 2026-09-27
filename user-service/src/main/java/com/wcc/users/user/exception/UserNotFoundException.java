package com.wcc.users.user.exception;

import com.wcc.commons.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "No profile exists for this user");
    }
}
