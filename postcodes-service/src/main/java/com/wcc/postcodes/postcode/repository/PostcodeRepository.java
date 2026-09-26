package com.wcc.postcodes.postcode.repository;

import com.wcc.postcodes.postcode.model.Postcode;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostcodeRepository extends MongoRepository<Postcode, String> {

    Optional<Postcode> findByPostcode(String postcode);
}
