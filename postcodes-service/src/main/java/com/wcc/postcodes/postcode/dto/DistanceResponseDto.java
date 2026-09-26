package com.wcc.postcodes.postcode.dto;

public record DistanceResponseDto(PostcodeDto from, PostcodeDto to, double distance, String unit) {
}
