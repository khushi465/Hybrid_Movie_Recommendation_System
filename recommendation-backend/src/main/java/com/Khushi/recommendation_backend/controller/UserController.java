package com.Khushi.recommendation_backend.controller;

import com.Khushi.recommendation_backend.dto.UserRequestDTO;
import com.Khushi.recommendation_backend.dto.UserResponseDTO;
import com.Khushi.recommendation_backend.model.User;
import com.Khushi.recommendation_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO request){
        return userService.createUser(request);
    }
    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @GetMapping
    public List<UserResponseDTO> getUsers(){
        return userService.getUsers();
    }
}
