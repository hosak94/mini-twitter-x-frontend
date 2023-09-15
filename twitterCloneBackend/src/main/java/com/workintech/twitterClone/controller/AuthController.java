package com.workintech.twitterClone.controller;

import com.workintech.twitterClone.dto.LoginRequest;
import com.workintech.twitterClone.dto.LoginResponse;
import com.workintech.twitterClone.dto.RegistrationMember;
import com.workintech.twitterClone.entity.Member;
import com.workintech.twitterClone.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthenticationService authenticationService;
    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public Member register(@RequestBody RegistrationMember registrationMember){
        return authenticationService.register(
                registrationMember.getEmail(),
                registrationMember.getPassword(),
                registrationMember.getUsername(),
                registrationMember.getFirstName(),
                registrationMember.getLastName());
    }

    @PostMapping("/login")
    public LoginResponse register(@RequestBody LoginRequest loginRequest){
        return authenticationService.login(loginRequest.getEmail(),loginRequest.getPassword());
    }
    @GetMapping("/logout")
    public String logout (HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login?logout";
    }
}
