package com.example.gamefiesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class TournamentService {
    private final TournamentRepository repo;
    private final BracketRepository bracketRepository;

    private final MatchRepository matchRepository;

    @Autowired
    public TournamentService(TournamentRepository repository, BracketRepository bracketRepository, MatchRepository matchRepository) {
        this.repo = repository;
        this.bracketRepository = bracketRepository;
        this.matchRepository = matchRepository;
    }

    public void addTournament(Tournament tournament) {
        repo.save(tournament);
    }

    public void addBracket(Bracket bracket) {
        bracketRepository.save(bracket);
    }

    public void addMatch(Match match) {
        matchRepository.save(match);
    }

    public void removeTournament(Tournament tournament){
        if(tournament.getBracketId() != null){
            if(bracketRepository.findById(tournament.getBracketId()).isPresent()) {
                if (!bracketRepository.findById(tournament.getBracketId()).get().getListOfMatches().isEmpty()) {
                    for (String matchId : bracketRepository.findById(tournament.getBracketId()).get().getListOfMatches()) {
                        matchRepository.deleteById(matchId);
                    }
                }
                bracketRepository.deleteById(tournament.getBracketId());
            }
        }
        repo.delete(tournament);
    }

    public Bracket generateBracketBasedOnBracketType(String bracketType){
        if (bracketType.equals("classic")) {
            return new ClassicBracket();
        }
        return new Bracket(bracketType);
    }
}
