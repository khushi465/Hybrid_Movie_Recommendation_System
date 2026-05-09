package com.Khushi.recommendation_backend.controller;

import com.Khushi.recommendation_backend.dto.LoginRequestDTO;
import com.Khushi.recommendation_backend.dto.RegisterRequestDTO;
import com.Khushi.recommendation_backend.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @GetMapping
    public String getting(){
        return "YAY"+"myverysecurejwtsecretkeyforhs256algorithm123456";
    }
    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequestDTO request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequestDTO request
    ) {
        return authService.login(request);
    }
}