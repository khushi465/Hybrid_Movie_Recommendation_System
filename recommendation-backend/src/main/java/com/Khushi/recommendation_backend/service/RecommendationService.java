package com.Khushi.recommendation_backend.service;

import com.Khushi.recommendation_backend.dto.MovieResponseDTO;
import com.Khushi.recommendation_backend.exception.UserNotFoundException;
import com.Khushi.recommendation_backend.model.Interaction;
import com.Khushi.recommendation_backend.model.Movie;
import com.Khushi.recommendation_backend.model.User;
import com.Khushi.recommendation_backend.repository.InteractionRepository;
import com.Khushi.recommendation_backend.repository.MovieRepository;
import com.Khushi.recommendation_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class RecommendationService {
private static final Logger log=LoggerFactory.getLogger(RecommendationService.class);
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final InteractionRepository interactionRepository;
    private final MLClientService mlClientService;
    private final MovieService movieService;

    @Cacheable(
            value = "recommendations",
            key = "'user:' + #userId",
            unless = "#result == null || #result.isEmpty() || #result.size() < 3"
    )
    public List<MovieResponseDTO> getRecommendations(Long userId, boolean includeWatched){
        long start=System.currentTimeMillis();
        log.info("Cache miss-> computing recommendations for user {}", userId);
        userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User with id "+userId+" not found"));
        List<Movie> allMovies=movieRepository.findAll();
        if(allMovies.isEmpty()){
            throw new RuntimeException("No movies available in system");
        }
        List<Interaction> allInteractions=interactionRepository.findRecentInteractions(PageRequest.of(0,500));
//        only latest 500 interactions
        List<Long> rankedIds= mlClientService.getRankedMovies(userId, allMovies, allInteractions);
        log.info("Ranked ids from ML: {}", rankedIds);
        if(rankedIds==null||rankedIds.isEmpty()){
            log.warn("ML failed-> using fallback for user {}", userId);
            return getFallbackMovies(userId);
        }

        Map<Long, Movie> movieMap=new HashMap<>();
        for(Movie m: allMovies){
            movieMap.put(m.getId(),m);
        }
        rankedIds = rankedIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .filter(movieMap::containsKey)
                .toList();
        Map<Long, Integer> rankIndex = new HashMap<>();
        for (int i = 0; i < rankedIds.size(); i++) {
            rankIndex.put(rankedIds.get(i), i);
        }

        List<Movie> result = allMovies.stream()
                .filter(m -> rankIndex.containsKey(m.getId()))
                .sorted(Comparator.comparingInt(m -> rankIndex.get(m.getId())))
                .toList();
        // 🔥 STEP: Watched filtering
        List<Interaction> userInteractions = allInteractions.stream()
                .filter(i -> i.getUserId().equals(userId))
                .toList();
        Set<Long> watched=userInteractions.stream()
                .map(Interaction::getMovieId)
                .collect(Collectors.toSet());
        log.info("Result BEFORE filtering: {}", result.stream().map(Movie::getId).toList());
        log.info("User {} watched movies: {}", userId, watched);
// check if user watched everything
        boolean allWatched = watched.size() >= allMovies.size();

// remove watched movies if not all watched
        if(allWatched){
            log.warn("User watched all movies → fallback to trending");
            return getFallbackMovies(userId);
        }
//        if(!allWatched){
//            result.removeIf(movie->watched.contains(movie.getId()));
//        }
        if(result.isEmpty()){
            log.warn("All recommendations filtered -> fallback for user {}", userId);
            return getFallbackMovies(userId);
        }
        if(result.size() < 10){
            List<MovieResponseDTO> fallback = getFallbackMovies(userId);

            Set<Long> existing = result.stream()
                    .map(Movie::getId)
                    .collect(Collectors.toSet());

            for(MovieResponseDTO m : fallback){
                if(!existing.contains(m.getId())){
                    result.add(movieMap.get(m.getId()));
                }
                if(result.size() >= 10) break;
            }
        }
        result= result.stream().limit(10).toList();
        log.info("Final recommendations: {}",
                result.stream().map(Movie::getId).toList()
        );
        List<MovieResponseDTO> response=new ArrayList<>();
        for(Movie movie:result){
            response.add(new MovieResponseDTO(movie.getId(), movie.getTitle(), movie.getGenre(), movie.getTags(), movie.getPopularityScore()));
        }
        long end=System.currentTimeMillis();
        log.info("Recommendation latency: {} ms", (end - start));
        return response;

    }
    private List<MovieResponseDTO> getFallbackMovies(Long userId) {

        Set<Long> watched = interactionRepository.findByUserId(userId)
                .stream()
                .map(Interaction::getMovieId)
                .collect(Collectors.toSet());

        List<Movie> allMovies = movieRepository.findAll()
                .stream()
                .filter(m -> Set.of(1L,2L,3L,5L,6L,7L,10L,16L).contains(m.getId()))
                .toList();

        // Step 1: filter unwatched
        List<Movie> filtered = allMovies.stream()
                .filter(m -> !watched.contains(m.getId()))
                .toList();

        // Step 2: if user watched everything → allow repeats
        if (filtered.isEmpty()) {
            filtered = allMovies;
        }

        // Step 3: sort + limit
        return filtered.stream()
                .sorted(Comparator.comparingDouble(Movie::getPopularityScore).reversed())
                .limit(10)
                .map(m -> new MovieResponseDTO(
                        m.getId(),
                        m.getTitle(),
                        m.getGenre(),
                        m.getTags(),
                        m.getPopularityScore()
                ))
                .toList();
    }
}
