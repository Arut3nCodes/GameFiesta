package com.example.gamefiesta.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.gamefiesta.UserRepository;
import com.example.gamefiesta.Users;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService service;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @ModelAttribute RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }



        @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(
        @ModelAttribute AuthenticationRequest request
    ) {

            AuthenticationResponse authRes = service.authenticate(request);
            if(authRes != null){
            ResponseCookie springCookie = ResponseCookie.from("user-id", authRes.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(3600)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "http://localhost:8080/");
            headers.add(HttpHeaders.SET_COOKIE, springCookie.toString());
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
            }
            return null;
        }


        @PostMapping("/changePassword")
        public ResponseEntity<String> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
            AuthenticationRequest authReq = new AuthenticationRequest(SecurityContextHolder.getContext().getAuthentication().getName(),oldPassword);
            AuthenticationResponse authRes = service.authenticate(authReq);
            if(authRes != null){
                Optional<Users> user = repository.findUsersByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if(user.isPresent()){
                    user.get().setPassword(passwordEncoder.encode(newPassword));
                    repository.save(user.get());
                }
            }
            return null;
        }
        

}
