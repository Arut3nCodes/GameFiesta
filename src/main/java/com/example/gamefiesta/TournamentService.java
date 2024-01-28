package com.example.gamefiesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class TournamentService {
    private final TournamentRepository repo;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TournamentService(TournamentRepository repository, MongoTemplate mongoTemplate) {
        this.repo = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public void addTournament(Tournament tournament) {
        repo.save(tournament);
    }

    public void addBracket(Bracket bracket) {
        mongoTemplate.save(bracket);
    }

    public void addMatch(Match match) {
        mongoTemplate.save(match);
    }
}
