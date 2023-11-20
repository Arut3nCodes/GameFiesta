package com.example.gamefiesta;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<Users, String> {

    Optional<Users> findUsersByUsername(String username);

    
}