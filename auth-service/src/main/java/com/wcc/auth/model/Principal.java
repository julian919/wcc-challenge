package com.wcc.auth.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("principals")
public record Principal(@Id String id, PrincipalType type, String secretHash, List<String> roles, List<Login> logins) {

    public enum PrincipalType {
        CLIENT, USER
    }

    public enum LoginType {
        CLIENT_ID, USERNAME
    }

    public record Login(LoginType type, String login) {
    }
}
