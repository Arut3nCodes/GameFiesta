package com.example.gamefiesta;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SquadRepository extends MongoRepository<Squad, String> {

}