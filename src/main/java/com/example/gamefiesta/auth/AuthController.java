package com.example.gamefiesta.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.gamefiesta.Users;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/register")
    public String registerForm(HttpServletRequest request, Model model) {
        Users user = new Users();
        model.addAttribute("user", user);
        if (request.getCookies() != null) {
            return "redirect:/";
        }
        return "register";
    }

    @GetMapping("/login")
    public String registerForm(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return "redirect:/";
        }
        return "login";
    }

}
