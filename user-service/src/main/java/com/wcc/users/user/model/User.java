package com.wcc.users.user.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public record User(@Id String id, String principalId, String firstName, String lastName, Instant createdAt) {
}
