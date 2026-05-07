package com.Khushi.recommendation_backend.service;

import com.Khushi.recommendation_backend.dto.UserRequestDTO;
import com.Khushi.recommendation_backend.dto.UserResponseDTO;
import com.Khushi.recommendation_backend.exception.UserNotFoundException;
import com.Khushi.recommendation_backend.model.User;
import com.Khushi.recommendation_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO request){
        if(request.getName()==null||request.getEmail()==null){
            throw new IllegalArgumentException("User name and email are required");
        }
//        if(userRepository.findByEmail(request.getEmail()).isPresent()){
//            throw new RuntimeException("Email already exists");
//        }
        User user=new User();

        user.setInteractionCount(0);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        User saved= userRepository.save(user);
        return new UserResponseDTO(saved.getId(),
                saved.getName(), saved.getEmail(), saved.getInteractionCount());
    }
    public UserResponseDTO getUser(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User with id "+id+" not found"));
        return new UserResponseDTO(
                user.getId(),user.getName(), user.getEmail(), user.getInteractionCount()
        );
    }

    public List<UserResponseDTO> getUsers(){
        List<User> users=userRepository.findAll();
        List<UserResponseDTO> response=new ArrayList<>();
        for(User u:users){
            response.add(new UserResponseDTO(u.getId(), u.getName(), u.getEmail(), u.getInteractionCount()));
        }
        return response;
    }
}
