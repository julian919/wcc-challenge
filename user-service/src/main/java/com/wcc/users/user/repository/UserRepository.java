package com.wcc.users.user.repository;

import com.wcc.users.user.model.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByPrincipalId(String principalId);
}
