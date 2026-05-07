package com.Khushi.recommendation_backend.service;

import com.Khushi.recommendation_backend.dto.InteractionRequestDTO;
import com.Khushi.recommendation_backend.dto.InteractionResponseDTO;
import com.Khushi.recommendation_backend.dto.MovieResponseDTO;
import com.Khushi.recommendation_backend.exception.UserNotFoundException;
import com.Khushi.recommendation_backend.model.Interaction;
import com.Khushi.recommendation_backend.model.Movie;
import com.Khushi.recommendation_backend.model.User;
import com.Khushi.recommendation_backend.repository.InteractionRepository;
import com.Khushi.recommendation_backend.repository.MovieRepository;
import com.Khushi.recommendation_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionService {
    private final InteractionRepository interactionRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @CacheEvict(value="recommendations", key="'user:' + #request.userId")
    @Transactional
    public InteractionResponseDTO saveInteraction(InteractionRequestDTO request){
        if (request.getUserId() == null || request.getMovieId() == null) {
            throw new IllegalArgumentException("UserId and MovieId are required");
        }
        if(request.getScore()==null){
            throw new IllegalArgumentException("Score is required");
        }
        System.out.println("Incoming movieId: "+request.getMovieId());
        Interaction interaction = new Interaction();
        interaction.setUserId(request.getUserId());
        interaction.setMovieId(request.getMovieId());
        interaction.setType(request.getType());
        interaction.setScore(request.getScore());
//        interaction.setTimestamp(LocalDateTime.now());
        interaction.setTimestamp(LocalDateTime.now());
        //update user interaction count for dynamic weighting
        User user=userRepository.findById(interaction.getUserId())
                .orElseThrow(()->new UserNotFoundException("User with id "+interaction.getUserId()+" Not Found"));
        Movie movie = movieRepository.findById(interaction.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Interaction saved = interactionRepository.save(interaction);

        movie.setPopularityScore(movie.getPopularityScore() + 1);
        movieRepository.save(movie);

        user.setInteractionCount(user.getInteractionCount() + 1);
        userRepository.save(user);

        return new InteractionResponseDTO(
                saved.getId(),
                saved.getUserId(),
                saved.getMovieId(),
                saved.getType(),
                saved.getScore(),
                saved.getTimestamp()
        );
    }
    public List<InteractionResponseDTO> getAllInteractions(){
        return interactionRepository.findAll()
                .stream()
                .map(i -> new InteractionResponseDTO(
                        i.getId(),
                        i.getUserId(),
                        i.getMovieId(),
                        i.getType(),
                        i.getScore(),
                        i.getTimestamp()
                )).toList();
    }
    public List<InteractionResponseDTO> getByUser(Long userId){
        return interactionRepository.findByUserId(userId)
                .stream()
                .map(i -> new InteractionResponseDTO(
                        i.getId(),
                        i.getUserId(),
                        i.getMovieId(),
                        i.getType(),
                        i.getScore(),
                        i.getTimestamp()
                ))
                .toList();
    }
    public InteractionResponseDTO getInteraction(Long id){
        Interaction i= interactionRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Interaction with id "+id+" not found"));
        return new InteractionResponseDTO(i.getId(),
                i.getUserId(),
                i.getMovieId(),
                i.getType(),
                i.getScore(),
                i.getTimestamp() );
    }
    }

