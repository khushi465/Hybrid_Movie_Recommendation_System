package com.Khushi.recommendation_backend.controller;

import com.Khushi.recommendation_backend.dto.MovieRequestDTO;
import com.Khushi.recommendation_backend.dto.MovieResponseDTO;
import com.Khushi.recommendation_backend.model.Movie;
import com.Khushi.recommendation_backend.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    @PostMapping
    public MovieResponseDTO addMovie(@Valid @RequestBody MovieRequestDTO movie){

        return movieService.addMovie(movie);
    }
    @GetMapping
    public List<MovieResponseDTO> getAllMovies() {
        return movieService.getAllMovies();
    }
    @GetMapping("/{id}")
    public MovieResponseDTO getMovie(@PathVariable Long id){
        return movieService.getMovie(id);
    }

}
