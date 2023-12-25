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


    @GetMapping("/myTeams")
    public String myTeams(Model model){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users uu = (Users)((UserDetails) user);
        model.addAttribute("teams", teamRepository.findByPlayers(uu.get_id()));
        return "myTeams";
    }

    @PostMapping("/checkUser")
    @ResponseBody
    public Boolean checkUser(@RequestParam String username) {
        Optional<Users> user = userRepository.findUsersByUsername(username);
        return user.isPresent();
    }


    @PostMapping("/inviteToTeam")
    @ResponseBody
    public Boolean inviteToTeam(@RequestParam String username,@RequestParam String teamID) {
        Optional<Users> userOptional = userRepository.findUsersByUsername(username);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            String userId = user.get_id();
    
            // Fetch the team from the database
            Optional<Team> teamOptional = teamRepository.findById(teamID);
    
            if (teamOptional.isPresent()) {
                Team team = teamOptional.get();
    
                // Check if the user is already a member of the team
                boolean isAlreadyMember = team.getPlayers().contains(userId);
    
                if (!isAlreadyMember) {
                    // Check if the invite already exists in the user's inbox
                    boolean inviteExists = user.getInbox().stream()
                            .anyMatch(invite -> "team".equals(invite.getType()) &&
                                    teamID.equals(invite.getSource()) &&
                                    userId.equals(invite.getDestination()));
    
                    if (!inviteExists) {
                        // If the invite doesn't exist, create it and add it to the user's inbox
                        Inbox invite = new Inbox("team", teamID, userId);
                        user.getInbox().add(invite);
    
                        // Save the updated user with the new invite
                        userRepository.save(user);
    
                        if (team.getInvitedList() == null) {
                            team.setInvitedList(new ArrayList<>());
                        }
                        team.getInvitedList().add(userId);
                        teamRepository.save(team);
                        // Return true to indicate that the invite was successfully created and added
                        return true;
                    } else {
                        // Return false to indicate that the invite already exists
                        return false;
                    }
                } else {
                    // Return false to indicate that the user is already a member of the team
                    return false;
                }
            } else {
                // Handle the case where the team doesn't exist
                return false;
            }
        } else {
            // Return false if the user doesn't exist
            return false;
        }
    }

}
