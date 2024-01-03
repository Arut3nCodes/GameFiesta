package com.example.gamefiesta.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.gamefiesta.JwtService;
import com.example.gamefiesta.UserRepository;
import com.example.gamefiesta.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AuthenticationResponse register(RegisterRequest request) {
        var user = Users.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
        .builder()
        .token(jwtToken)
        .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try{
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        }catch(BadCredentialsException e){
            return null;
        }

        var user = repository.findUsersByUsername(request.getUsername()).orElseThrow();  // Zajac sie tym potem
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse
        .builder()
        .token(jwtToken)
        .build();
    }
    
}
