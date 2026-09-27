package com.wcc.users.user.dto;

import com.wcc.users.user.model.User;
import java.time.Instant;

public record UserDto(String id, String principalId, String firstName, String lastName, Instant createdAt) {

    public static UserDto from(User user) {
        return new UserDto(user.id(), user.principalId(), user.firstName(), user.lastName(), user.createdAt());
    }
}
