package com.wcc.auth.repository;

import com.wcc.auth.model.Principal;
import com.wcc.auth.model.Principal.LoginType;
import com.wcc.auth.model.Principal.PrincipalType;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PrincipalRepository extends MongoRepository<Principal, String> {

    @Query("{ 'type': ?0, 'logins': { $elemMatch: { 'type': ?1, 'login': ?2 } } }")
    Optional<Principal> findByTypeAndLogin(PrincipalType principalType, LoginType loginType, String login);

    void deleteByIdAndType(String id, PrincipalType type);
}
