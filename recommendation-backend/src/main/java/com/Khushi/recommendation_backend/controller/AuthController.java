//package com.Khushi.recommendation_backend.controller;
//
//import com.Khushi.recommendation_backend.dto.LoginRequestDTO;
//import com.Khushi.recommendation_backend.dto.LoginResponseDTO;
//import com.Khushi.recommendation_backend.dto.RegisterRequestDTO;
//import com.Khushi.recommendation_backend.exception.UserNotFoundException;
//import com.Khushi.recommendation_backend.model.User;
//import com.Khushi.recommendation_backend.repository.UserRepository;
//import com.Khushi.recommendation_backend.security.JWTService;
//import com.Khushi.recommendation_backend.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthController {
//    private final UserRepository userRepository;
//    private final JWTService jwtService;
//    private final UserService userService;
//
//    @PostMapping("/login")
//    public LoginResponseDTO login(@RequestBody LoginRequestDTO request){
//        User user=userRepository.findByEmail(request.getEmail())
//                .orElseThrow(()->new UserNotFoundException("User with email "+request.getEmail()+" not found"));
//
//        //for now plain password
//        if(!user.getPassword().equals(request.getPassword())){
//            throw new RuntimeException("Invalid credentials");
//        }
//        String token=jwtService.generateToken(user.getEmail());
//        return new LoginResponseDTO(token);
//    }
//
//    @PostMapping("/register")
//    public String register(@RequestBody RegisterRequestDTO request){
//        if(userRepository.findByEmail(request.getEmail()).isPresent()){
//            throw new RuntimeException("Email already exists");
//        }
//        userService.createUser()
//    }
//}
