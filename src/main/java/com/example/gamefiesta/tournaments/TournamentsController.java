package com.example.gamefiesta.tournaments;

import com.example.gamefiesta.*;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.core.support.FragmentNotImplementedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentsController {
    
    private final TournamentRepository tournamentRepository;
    private final BracketRepository bracketRepository;
    private final TournamentService service;

    @GetMapping("")
    public String showTournaments(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest pageable = PageRequest.of(page, 10); // Page size is 10
        Page<Tournament> ppage = tournamentRepository.findAll(pageable);
        model.addAttribute("page", ppage);
        return "tournamentsWithPages";
    }

    @GetMapping("/{tournamentId}")
    public String showTournament(Model model,@PathVariable String tournamentId){
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if(tournament.isPresent()){
            Tournament ttournament = tournament.get();
            if(ttournament.getBracketId() != null) {
                Optional<Bracket> bracket = bracketRepository.findById(ttournament.getBracketId());
                if (bracket.isPresent()) {
                    ttournament.setBracket(bracket.get());
                }
            }
            model.addAttribute("tournament", ttournament);
            model.addAttribute("numberOfTeams", ttournament.getListOfSquads()!=null ? ttournament.getListOfSquads().size() : 0);
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
            Bracket bracket = service.generateBracketBasedOnBracketType(formDTO.getBracketType().toLowerCase());
            bracket = bracketRepository.save(bracket);
            Tournament tournament = new Tournament(
                    formDTO.getTournamentName(),
                    bracket,
                    formDTO.getPlayerCount(),
                    formatter.parse(formDTO.getDate()),
                    formDTO.getShortDescription(),
                    formDTO.getDescription()
            );

            service.addTournament(tournament);
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
            service.removeTournament(tournamentRepository.findById(tournamentId).get());
        }
        return "redirect:/tournaments";
    }

    @PostMapping("/generateBracket")
    public String generateBracket(@RequestParam String tournamentId){
        throw new UnsupportedOperationException("Method not implemented yet");
    }

}
