package com.example.gamefiesta.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
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
    
    public AuthenticatioResponse register(RegisterRequest request) {
        var user = Users.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticatioResponse
        .builder()
        .token(jwtToken)
        .build();
    }

    public AuthenticatioResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = repository.findUsersByUsername(request.getUsername()).orElseThrow();  // Zaajc sie tym potem
                var jwtToken = jwtService.generateToken(user);
        return AuthenticatioResponse
        .builder()
        .token(jwtToken)
        .build();
    }
    
}
