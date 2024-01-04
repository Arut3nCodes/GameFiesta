package com.example.gamefiesta;

import java.util.List;


import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
    List<Team> findByPlayers(String userID);
    Optional<Team> findTeamByName(String name);
}