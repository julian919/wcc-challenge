package com.wcc.postcodes.postcode.exception;

import com.wcc.postcodes.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class PostcodeNotFoundException extends ApiException {

    public PostcodeNotFoundException(String postcode) {
        super(HttpStatus.NOT_FOUND, "POSTCODE_NOT_FOUND", "Postcode not found: " + postcode);
    }
}
