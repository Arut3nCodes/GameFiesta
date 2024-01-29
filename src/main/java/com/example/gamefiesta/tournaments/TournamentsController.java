package com.example.gamefiesta.tournaments;

import com.example.gamefiesta.TournamentDTO;
import com.example.gamefiesta.TournamentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import com.example.gamefiesta.Tournament;
import com.example.gamefiesta.TournamentRepository;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentsController {
    
    private final TournamentRepository repository;
    private final TournamentService service;

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

    @GetMapping("/createTournament")
        public String createTournament(){
            return "createTournament";
        }

    @PostMapping("/createTournament")
        public String createTournamentSend(TournamentDTO formDTO, Model model){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Tournament tournament = new Tournament(
                    formDTO.getTournamentName(),
                    formDTO.getBracketType(),
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
}
