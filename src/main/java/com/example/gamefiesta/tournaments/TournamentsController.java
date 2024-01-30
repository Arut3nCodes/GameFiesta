package com.example.gamefiesta.tournaments;

import com.example.gamefiesta.*;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.core.support.FragmentNotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentsController {
    
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final BracketRepository bracketRepository;
    private final SquadRepository squadRepository;
    private final TeamRepository teamRepository;
    private final TournamentService tournamentService;

    @GetMapping("")
    public String showTournaments(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest pageable = PageRequest.of(page, 10); // Page size is 10
        Page<Tournament> ppage = tournamentRepository.findAll(pageable);
        model.addAttribute("page", ppage);
        return "tournamentsWithPages";
    }

    @GetMapping("/{tournamentId}")
    public String showTournament(Model model,@PathVariable String tournamentId){
        Tournament tournament = tournamentService.getTournament(tournamentId);
        if(tournament != null){
            model.addAttribute("tournament", tournament);
            model.addAttribute("numberOfTeams", tournament.getListOfSquads()!=null ? tournament.getListOfSquads().size() : 0);
            model.addAttribute("teamRepository", teamRepository.findAllById(
                    squadRepository.findAllById(tournament.getListOfSquads())
                    .stream()
                    .map(Squad::getTeam)
                            .toList()

            ));
            return "tournament";
        }
        return "index";
    }

    @GetMapping("/createTournament")
        public String createTournament(){
            return "createTournament";
        }

    @PostMapping("/createTournament")
        public String createTournamentSend(TournamentDTO formDTO, Model model){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Bracket bracket = tournamentService.generateBracketBasedOnBracketType(formDTO.getBracketType().toLowerCase());
            bracket = bracketRepository.save(bracket);
            Tournament tournament = new Tournament(
                    formDTO.getTournamentName(),
                    formDTO.getOrganizerId(),
                    bracket,
                    formDTO.getPlayerCount(),
                    formatter.parse(formDTO.getDate()),
                    formDTO.getShortDescription(),
                    formDTO.getDescription()
            );

            tournamentService.addTournament(tournament);
        }catch(Exception e){
            System.out.println("xdd");
        }
        return "redirect:/tournaments";
    }

    @GetMapping("/tournamentsWithPages")
    public String paginatedMongoPage(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest pageable = PageRequest.of(page, 10); // Page size is 10
        Page<Tournament> ppage = tournamentRepository.findAll(pageable);
        model.addAttribute("page", ppage);
        return "tournamentsWithPages";
    }

    @PostMapping
    public String handleDeleteForm(@RequestParam String tournamentId){
        if(tournamentRepository.findById(tournamentId).isPresent()) {
            tournamentService.removeTournament(tournamentRepository.findById(tournamentId).get());
        }
        return "redirect:/tournaments";
    }

    @PostMapping("/generateBracket")
    public String generateBracket(@RequestParam String tournamentId){

        Tournament tournament = tournamentService.getTournament(tournamentId);
        
        if(tournament.getBracketId() != null && !tournament.getListOfTeams().isEmpty()) {
            Optional<Bracket> bracket = bracketRepository.findById(tournament.getBracketId());
            if (bracket.isPresent()) {
                Bracket newBracket = tournamentService.generateBracketBasedOnBracketType(bracket.get().getType());
                newBracket = bracketRepository.save(newBracket);
                tournament.setBracketId(newBracket.get_id());
                tournament.setBracket(newBracket);
                tournament.getBracket().generateRandomLadder((ArrayList<String>)tournament.getListOfTeams());
                tournament.getBracket().setListOfMatchObjects(matchRepository.saveAll(tournament.getBracket().getListOfMatchObjects()));
                tournamentService.removeBracket(bracket.get());
                bracketRepository.save(tournament.getBracket());
                tournamentRepository.save(tournament);
            }
            else{
                Bracket newBracket = tournamentService.generateBracketBasedOnBracketType("classic");
                newBracket = bracketRepository.save(newBracket);
                tournament.setBracketId(newBracket.get_id());
                tournament.setBracket(newBracket);
                tournament.getBracket().generateRandomLadder((ArrayList<String>)tournament.getListOfTeams());
                tournament.getBracket().setListOfMatchObjects(matchRepository.saveAll(tournament.getBracket().getListOfMatchObjects()));
                bracketRepository.save(tournament.getBracket());
                tournamentRepository.save(tournament);
            }
        }

        
        return "redirect:/tournaments/" + tournamentId;
    }

    @PostMapping("{tournamentId}/{matchId}/startMatch")
    public String startMatch(@PathVariable String tournamentId, @PathVariable String matchId){
        List<Match> matches = matchRepository.findAll();
        Match match = matches.stream().filter(m -> m.get_id().equals(matchId)).findFirst().get();
        if(match.startMatch()) {
            matchRepository.save(match);
        }
        return "redirect:/tournaments/" + tournamentId;
    }

    @PostMapping("{tournamentId}/{matchId}/addPointToTeamA")
    public String addPointToTeamA(@PathVariable String tournamentId, @PathVariable String matchId) {
        List<Match> matches = matchRepository.findAll();
        Match match = matches.stream().filter(m -> m.get_id().equals(matchId)).findFirst().get();
        if (match != null && "IN_PROGRESS".equals(match.getStatus())) {
            match.addPointToTeamA();
            matchRepository.save(match);
        }
        return "redirect:/tournaments/" + tournamentId;
    }

    @PostMapping("/{tournamentId}/{matchId}/addPointToTeamB")
    public String addPointToTeamB(@PathVariable String tournamentId, @PathVariable String matchId) {
        List<Match> matches = matchRepository.findAll();
        Match match = matches.stream().filter(m -> m.get_id().equals(matchId)).findFirst().get();
        if (match != null && "IN_PROGRESS".equals(match.getStatus())) {
            match.addPointToTeamB();
            matchRepository.save(match);
        }
        return "redirect:/tournaments/" + tournamentId;
    }
    @PostMapping("/{tournamentId}/{matchId}/autoSetVictor")
    public String autoSetVictor(@PathVariable String tournamentId, @PathVariable String matchId) {
        List<Match> matches = matchRepository.findAll();
        Bracket bracket = tournamentService.getTournament(tournamentId).getBracket();
        Match match = matches.stream().filter(m -> m.get_id().equals(matchId)).findFirst().get();
        if (match != null && "IN_PROGRESS".equals(match.getStatus())) {
            Match another = bracket.autoAdvanceTeam(match);
            if (another != null) {
                matchRepository.save(match);
                matchRepository.save(another);
            }
        }
        return "redirect:/tournaments/" + tournamentId;
    }

    @PostMapping("/{tournamentId}/{matchId}/setVictorManually")
    public String setVictorManually(@PathVariable String tournamentId, @PathVariable String matchId){
        List<Match> matches = matchRepository.findAll();
        Match match = matches.stream().filter(m -> m.get_id().equals(matchId)).findFirst().get();
        if (match != null && "IN_PROGRESS".equals(match.getStatus())) {
            match.setVictor(true); // Replace 'true' with your logic for manual victor setting
            matchRepository.save(match);
        }
        return "redirect:/tournaments/" + tournamentId;
    }
}
