package com.example.gamefiesta.tournaments;

import java.util.List;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;
import com.example.gamefiesta.Tournament;
import com.example.gamefiesta.TournamentRepository;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentsController {
    
    private final TournamentRepository repository;

    @GetMapping("")
    public String showTournaments(Model model){
        model.addAttribute("tournaments", repository.findAll());
        return "tournaments";
    }

    @GetMapping("/{tournamentId}")
    public String showTournament(Model model,@PathVariable String tournamentId){
        Optional<Tournament> tournament = repository.findById(tournamentId);
        if(tournament.isPresent()){
            model.addAttribute("tournament", tournament.get());
            return "tournament";
        }
        return "index";
    }
}
