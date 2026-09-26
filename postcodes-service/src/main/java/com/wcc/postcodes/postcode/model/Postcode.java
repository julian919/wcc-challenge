package com.wcc.postcodes.postcode.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Postcodes")
public record Postcode(@Id String id, String postcode, double latitude, double longitude) {
}
