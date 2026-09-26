package com.wcc.postcodes.postcode.dto;

import com.wcc.postcodes.postcode.model.Postcode;

public record PostcodeDto(String postcode, double latitude, double longitude) {

    public static PostcodeDto from(Postcode postcode) {
        return new PostcodeDto(postcode.postcode(), postcode.latitude(), postcode.longitude());
    }
}
