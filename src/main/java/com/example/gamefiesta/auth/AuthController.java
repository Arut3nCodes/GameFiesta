package com.example.gamefiesta.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.gamefiesta.Users;

@Controller
@RequestMapping("/auth")
public class AuthController {
    

@GetMapping("/register")
public String registerForm(Model model){
    Users user = new Users();
    model.addAttribute("user", user);
    return "register";
}

}
