package com.wcc.auth.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("principals")
public record Principal(@Id String id, String type, String secretHash, List<String> roles, List<Login> logins) {

    public record Login(String type, String login) {
    }
}
