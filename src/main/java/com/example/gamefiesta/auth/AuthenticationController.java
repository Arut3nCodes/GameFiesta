package com.example.gamefiesta.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamefiesta.Users;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticatioResponse> register(
        @ModelAttribute RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }



        @PostMapping("/authenticate")
    public ResponseEntity<ResponseCookie> authenticate(
        @ModelAttribute AuthenticationRequest request
    ){

        AuthenticatioResponse authRes = service.authenticate(request);
        ResponseCookie springCookie = ResponseCookie.from("user-id", authRes.getToken())
                                        .httpOnly(true)
                                        .secure(true)
                                        .path("/")
                                        .maxAge(60)
                                        .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(springCookie);
    }

}
