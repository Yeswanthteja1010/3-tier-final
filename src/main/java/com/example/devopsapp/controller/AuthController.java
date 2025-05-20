package com.example.devopsapp.controller;

import com.example.devopsapp.model.User;
import com.example.devopsapp.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("msg", "You have been logged out.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Check if email or username already exists
        Optional<User> byEmail = userService.findByEmail(user.getEmail());
        Optional<User> byUsername = userService.findByUsername(user.getUsername());

        if (byEmail.isPresent()) {
            model.addAttribute("errorMsg", "Email already registered");
            return "register";
        }

        if (byUsername.isPresent()) {
            model.addAttribute("errorMsg", "Username already taken");
            return "register";
        }

        userService.saveUser(user);
        model.addAttribute("msg", "Registration successful! Please login.");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        User user = userOpt.get();

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        // DevOps materials (can be static, hardcoded here)
        model.addAttribute("materials", new String[]{
                "Docker Basics",
                "Kubernetes Tutorial",
                "Jenkins CI/CD",
                "Terraform Infrastructure as Code",
                "RabbitMQ Messaging",
                "Memcached Caching"
        });

        return "dashboard";
    }

}
