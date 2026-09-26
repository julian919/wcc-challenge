package com.wcc.auth.repository;

import com.wcc.auth.model.Role;
import java.util.Collection;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

    List<Role> findByNameIn(Collection<String> names);
}
