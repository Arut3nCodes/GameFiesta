package com.example.gamefiesta;
import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.example.gamefiesta.Tournament;
import com.example.gamefiesta.TournamentRepository;
import java.util.Collection;
import java.util.Set;
import java.util.List;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WebCrontroller {
    
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @GetMapping("")
    public String main(Model model){

        model.addAttribute("tournaments", tournamentRepository.findAll());
        
        return "index";
    }


    @GetMapping("/profile")
    public String myTeams(Model model){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users uu = (Users)((UserDetails) user);
        model.addAttribute("teams", teamRepository.findByPlayers(uu.get_id()));
        return "profile";
    }





}
