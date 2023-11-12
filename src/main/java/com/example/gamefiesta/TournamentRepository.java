package com.example.gamefiesta;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TournamentRepository extends MongoRepository<User, String> {

    
}