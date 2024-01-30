package com.example.gamefiesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final BracketRepository bracketRepository;

    private final SquadRepository squadRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public TournamentService(TournamentRepository repository, BracketRepository bracketRepository, SquadRepository squadRepository, MatchRepository matchRepository) {
        this.tournamentRepository = repository;
        this.bracketRepository = bracketRepository;
        this.squadRepository = squadRepository;
        this.matchRepository = matchRepository;
    }

    public void addTournament(Tournament tournament) {
        tournamentRepository.save(tournament);
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
        tournamentRepository.delete(tournament);
    }

    public void removeBracket(Bracket bracket){
        if (!bracket.getListOfMatches().isEmpty()) {
            for (String matchId : bracket.getListOfMatches()) {
                matchRepository.deleteById(matchId);
            }
        }
        bracketRepository.delete(bracket);
    }

    public Tournament getTournament(String tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isPresent()) {
            Tournament ttournament = tournament.get();
            if (ttournament.getBracketId() != null) {
                Optional<Bracket> bracket = bracketRepository.findById(ttournament.getBracketId());
                if (bracket.isPresent()) {
                    List<Match> matchList= matchRepository.findAll();
                    for(String _id: bracket.get().getListOfMatches()){
                        for(Match match: matchList){
                            if(match.get_id().equals(_id))
                                bracket.get().getListOfMatchObjects().add(match);
                        }
                    }
                    ttournament.setBracket(bracket.get());
                }
            }
            if(ttournament.getListOfSquads() != null){
                for(Squad squad: squadRepository.findAllById(ttournament.getListOfSquads())){
                    if(!ttournament.getListOfTeams().contains(squad.getTeam()))
                        ttournament.getListOfTeams().add(squad.getTeam());
                }
            }
            tournamentRepository.save(ttournament);
            return ttournament;
        }
        return null;
    }


    public Bracket generateBracketBasedOnBracketType(String bracketType){
        if (bracketType.equals("classic")) {
            return new ClassicBracket();
        }
        return new Bracket(bracketType);
    }
}
