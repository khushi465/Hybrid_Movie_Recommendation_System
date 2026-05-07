package com.Khushi.recommendation_backend.service;

import com.Khushi.recommendation_backend.dto.MovieRequestDTO;
import com.Khushi.recommendation_backend.dto.MovieResponseDTO;
import com.Khushi.recommendation_backend.dto.UserResponseDTO;
import com.Khushi.recommendation_backend.exception.UserNotFoundException;
import com.Khushi.recommendation_backend.model.Movie;
import com.Khushi.recommendation_backend.model.User;
import com.Khushi.recommendation_backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieResponseDTO addMovie(MovieRequestDTO request){
        if(request.getTitle()==null||request.getGenre()==null){
            throw new IllegalArgumentException("Movie title and genre are required");
        }
        Movie movie=new Movie();
        movie.setTitle(request.getTitle());
        movie.setGenre(request.getGenre());
        Movie saved=movieRepository.save(movie);
        return new MovieResponseDTO(saved.getId(),
                saved.getTitle(), saved.getGenre(), saved.getTags(), saved.getPopularityScore());

    }
    public List<MovieResponseDTO> getAllMovies(){
        List<Movie> movies= movieRepository.findAll();
        List<MovieResponseDTO> response=new ArrayList<>();
        for(Movie m:movies){
            response.add(new MovieResponseDTO(m.getId(), m.getTitle(), m.getGenre(), m.getTags(),m.getPopularityScore()));
        }
        return response;
    }
    public MovieResponseDTO getMovie(Long id){
        Movie movie= movieRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Movie with id "+id+" not found"));
        return new MovieResponseDTO(movie.getId(), movie.getTitle(), movie.getGenre(), movie.getTags(), movie.getPopularityScore());
    }
}
