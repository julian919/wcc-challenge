package com.wcc.auth.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("roles")
public record Role(@Id String id, String name, List<String> permissions) {
}
