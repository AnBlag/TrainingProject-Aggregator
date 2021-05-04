package ru.newsagregator.App.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("user:read"));
        if (hasUserRole) return "redirect:/meduza/news";
        else return "login";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) return "redirect:/meduza/news";
        else return "logout";
    }
}
