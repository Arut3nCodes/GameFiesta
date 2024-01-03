package com.example.gamefiesta.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService service;

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

}
