package com.example.gamefiesta;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

import org.springframework.boot.context.config.ConfigData.Option;
import org.springframework.http.ResponseEntity;
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
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class InviteController {
    

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final SquadRepository squadRepository;

    @PostMapping("/checkUser")
    @ResponseBody
    public Boolean checkUser(@RequestParam String username) {
        Optional<Users> user = userRepository.findUsersByUsername(username);
        return user.isPresent();
    }


    @PostMapping("/inviteToTeam")
    @ResponseBody
    public Boolean inviteToTeam(@RequestParam String username,@RequestParam String teamID) {

        Object invitingUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users invitingUserr = (Users)((UserDetails) invitingUser);

        Optional<Users> userOptional = userRepository.findUsersByUsername(username);
        Optional<Team> teamOptional = teamRepository.findById(teamID);
        if (userOptional.isPresent() && teamOptional.isPresent()) {
            Users user = userOptional.get();
            String userId = user.getUsername();
                Team team = teamOptional.get();
                boolean isAlreadyMember = team.getPlayers().contains(userId);

                if (!isAlreadyMember && team.getLeader().equals(invitingUserr.getUsername())){
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
                    }
                }
        } 
        return false;
    }


    @PostMapping("/inviteToTournament")
@ResponseBody
public ResponseEntity<String> inviteToTournament(
        @RequestParam String username,
        @RequestParam String tournamentId) {

    // Get the authenticated user (inviting user)
    Object invitingUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Users invitingUserEntity = (Users) ((UserDetails) invitingUser);

    // Find the user to be invited
    Optional<Users> userOptional = userRepository.findUsersByUsername(username);
    // Find the tournament
    Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);

    if (userOptional.isPresent() && tournamentOptional.isPresent()) {
        Users invitedUser = userOptional.get();
        String invitedUserId = invitedUser.getUsername();
        Tournament tournament = tournamentOptional.get();

        // Check if the inviting user is the leader of the tournament
        if (tournament.getOrganizer().equals(invitingUserEntity.getUsername())) {

            // Check if the user is already a participant
            // boolean isAlreadyParticipant = tournament.getParticipants().contains(invitedUserId);
            boolean isAlreadyParticipant = false;

            if (!isAlreadyParticipant) {
                // Check if the invitation already exists
                boolean inviteExists = invitedUser.getInbox().stream()
                        .anyMatch(invite -> "tournament".equals(invite.getType()) &&
                                tournamentId.equals(invite.getSource()) &&
                                invitedUserId.equals(invite.getDestination()));

                if (!inviteExists) {
                    // If the invite doesn't exist, create it and add it to the user's inbox
                    Inbox invite = new Inbox("tournament", tournamentId, invitedUserId);
                    invitedUser.getInbox().add(invite);

                    // Save the updated user with the new invite
                    userRepository.save(invitedUser);

                    // Update the tournament's invited list
                    if (tournament.getInvitedList() == null) {
                        tournament.setInvitedList(new ArrayList<>());
                    }
                    tournament.getInvitedList().add(invitedUserId);
                    tournamentRepository.save(tournament);

                    return ResponseEntity.ok("Player invited to the tournament.");
                } else {
                    return ResponseEntity.badRequest().body("Invitation already exists.");
                }
            } else {
                return ResponseEntity.badRequest().body("Player is already a participant in the tournament.");
            }
        } else {
            return ResponseEntity.badRequest().body("User is not the leader of the tournament.");
        }
    } else {
        return ResponseEntity.badRequest().body("User or tournament not found.");
    }
}


    @PostMapping("/joinTeam")
    @ResponseBody
    public Boolean joinTeam(@RequestParam String teamID) {
        Optional<Team> teamOptional = teamRepository.findById(teamID);
        Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = (Users)((UserDetails) userObj);
        String userId = user.getUsername();
        
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            // Check if there's a correct invite in the user's inbox
            Optional<Inbox> inviteOptional = user.getInbox().stream()
                    .filter(invite -> "team".equals(invite.getType()) &&
                            teamID.equals(invite.getSource()) &&
                            userId.equals(invite.getDestination()))
                    .findFirst();
    
            if (inviteOptional.isPresent()) {
                // Remove the user from the team's invitedList
                if (team.getInvitedList() != null && team.getInvitedList().contains(userId)) {
                    team.getInvitedList().remove(userId);
                    teamRepository.save(team);

                // Add the user to the team
                if (team.getPlayers() == null) {
                    team.setPlayers(new ArrayList<>());
                }
                team.getPlayers().add(userId);
                teamRepository.save(team);
    
                // Remove the invite from the user's inbox
                user.getInbox().remove(inviteOptional.get());
                userRepository.save(user);
    
                return true;
                }
    

            }
        }
    
        return false;

    }


    @PostMapping("/removeFromTeam")
    @ResponseBody
    public Boolean removeFromTeam(@RequestParam String playerID,@RequestParam String teamID) {
        Optional<Team> teamOptional = teamRepository.findById(teamID);

        if(teamOptional.isPresent()){
            Team team = teamOptional.get();
            if(team.getPlayers().contains(playerID)){
                team.getPlayers().remove(playerID);
                teamRepository.save(team);
                return true;
            }
        }
        return false;
    }

    @PostMapping("/removeInvitationFromTeam")
    @ResponseBody
    public Boolean removeInvitationFromTeam(@RequestParam String playerID,@RequestParam String teamID) {
        Optional<Team> teamOptional = teamRepository.findById(teamID);
        Optional<Users> userOptional = userRepository.findById(playerID);

        if(teamOptional.isPresent()){
            Team team = teamOptional.get();
            if(team.getInvitedList().contains(playerID)){
                team.getInvitedList().remove(playerID);
                teamRepository.save(team);


                if(userOptional.isPresent()){
                    Users user = userOptional.get();
            if (removeInvitationForTeam(user, teamID)) {
                // Save the changes to the user
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
                
        }

            }
        }

    
        return false;
    }
    


    //Zwraca teraz tylko zaproszenia do druzyny
    @PostMapping("/getInvites")
    @ResponseBody
    public List<Team> getInvites() {
        // Retrieve the authenticated user
        Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = (Users)((UserDetails) userObj);
        // Retrieve user's invites
        List<Team> teamInvites = user.getInbox().stream()
        .filter(invite -> "team".equals(invite.getType()))
        .map(Inbox::getSource)
        .map(teamId -> teamRepository.findById(teamId).orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

        return teamInvites;

    }

    @PostMapping("/getTournamentInvites")
    @ResponseBody
    public List<Tournament> getTournamentInvites() {
        // Retrieve the authenticated user
        Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = (Users)((UserDetails) userObj);

        List<Tournament> tournamentInvites = user.getInbox().stream()
        .filter(invite -> "tournament".equals(invite.getType()))
        .map(Inbox::getSource)
        .map(tournamentId -> tournamentRepository.findById(tournamentId).orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());


        return tournamentInvites;

    }


    @PostMapping("/joinTournament")
    @ResponseBody
    public Boolean joinTournament(@RequestBody Map<String, Object> requestData) {
        // Get the authenticated user
        Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = (Users) ((UserDetails) userObj);

        List<String> players = (List<String>) requestData.get("players");
        String teamId = (String) requestData.get("teamId");
        String tournamentId = (String) requestData.get("tournamentId");
        // Find the tournament by ID
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        if (tournamentOptional.isPresent()) {
            Tournament tournament = tournamentOptional.get();


            // Check if the user is invited to the tournament
            if (user.getInbox().stream()
                    .anyMatch(invite -> "tournament".equals(invite.getType()) &&
                            tournamentId.equals(invite.getSource()) &&
                            user.getUsername().equals(invite.getDestination()))) {

                // Check if the user is not already a participant
                if (!tournament.getListOfSquads().contains(user.get_id())) {
                    Squad squad = new Squad(tournamentId, teamId, players);
                    squadRepository.insert(squad);
                    tournament.getListOfSquads().add(squad.get_id());

                    

                    tournamentRepository.save(tournament);

                    user.setInbox(user.getInbox().stream()
                            .filter(invite -> !("tournament".equals(invite.getType()) &&
                                    tournamentId.equals(invite.getSource()) &&
                                    user.getUsername().equals(invite.getDestination())))
                            .collect(Collectors.toList()));
                    userRepository.save(user);

                    return true;
                }

            } 

        } 
        return false;
    }



    @PostMapping("/fetchTeams")
    @ResponseBody
    public List<Team> fetchTeams() {
        // Retrieve the authenticated user
        Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = (Users)((UserDetails) userObj);
        return teamRepository.findByLeader(user.getUsername());

    }



    @PostMapping("/addTeam")
    @ResponseBody
    public Boolean addTeam(@RequestParam String teamName, @RequestParam String user) {
        System.out.println(user);
        System.out.println(teamName);
        Optional<Team> teamOptional = teamRepository.findTeamByName(teamName);
        Optional<Users> userOptional = userRepository.findUsersByUsername(user);
        if(!teamOptional.isPresent() && userOptional.isPresent()){
            ArrayList<String> players = new ArrayList<>();
            players.add(userOptional.get().getUsername());
            Team newTeam = new Team(null, userOptional.get().getUsername(), teamName, players, new ArrayList<String>());
            teamRepository.insert(newTeam);
            return true;
        }
        return false;
    }
    

    private boolean removeInvitationForTeam(Users user, String teamId) {
        // Filter out the invitation for the specified team from the user's inbox
        List<Inbox> updatedInbox = user.getInbox().stream()
                .filter(invite -> !("team".equals(invite.getType()) && teamId.equals(invite.getSource())))
                .collect(Collectors.toList());

        // Check if any invitation was removed
        if (updatedInbox.size() < user.getInbox().size()) {
            user.setInbox(updatedInbox);
            return true;
        } else {
            return false;
        }
    }


}
