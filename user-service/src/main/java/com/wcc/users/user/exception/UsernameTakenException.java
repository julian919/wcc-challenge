package com.wcc.users.user.exception;

import com.wcc.commons.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UsernameTakenException extends ApiException {

    public UsernameTakenException(String username) {
        super(HttpStatus.CONFLICT, "USERNAME_TAKEN", "Username is already taken: " + username);
    }
}
