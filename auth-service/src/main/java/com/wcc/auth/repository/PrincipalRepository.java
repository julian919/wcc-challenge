package com.wcc.auth.repository;

import com.wcc.auth.model.Principal;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PrincipalRepository extends MongoRepository<Principal, String> {

    @Query("{ 'type': ?0, 'logins': { $elemMatch: { 'type': ?1, 'login': ?2 } } }")
    Optional<Principal> findByTypeAndLogin(String principalType, String loginType, String login);
}
