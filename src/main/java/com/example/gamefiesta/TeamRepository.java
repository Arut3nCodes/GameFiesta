package com.example.gamefiesta;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<User, String> {

    
}