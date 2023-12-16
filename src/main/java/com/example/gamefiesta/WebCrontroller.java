package com.example.gamefiesta;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.example.gamefiesta.Tournament;
import com.example.gamefiesta.TournamentRepository;

import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WebCrontroller {
    
    private final TournamentRepository tournamentRepository;

    @GetMapping("")
    public String main(Model model){

        model.addAttribute("tournaments", tournamentRepository.findAll());
        
        return "index";
    }
}
