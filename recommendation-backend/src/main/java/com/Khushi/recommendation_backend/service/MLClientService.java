package com.Khushi.recommendation_backend.service;

import com.Khushi.recommendation_backend.dto.MLInteractionDTO;
import com.Khushi.recommendation_backend.dto.MLMovieDTO;
import com.Khushi.recommendation_backend.model.Interaction;
import com.Khushi.recommendation_backend.model.Movie;
import com.Khushi.recommendation_backend.repository.MovieRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MLClientService {
    private static final Logger log = LoggerFactory.getLogger(MLClientService.class);
//            @Value("${ml.service.url}")
            private String url="http://ml-service:8000/recommend";
    private final RestTemplate restTemplate=new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(2))
            .setReadTimeout(Duration.ofSeconds(3))
            .build();
    private final MovieRepository movieRepository;
    @CircuitBreaker(name="mlService", fallbackMethod="fallbackRecommendations")
    public List<Long> getRankedMovies(Long userId, List<Movie> movies, List<Interaction> allInteractions){
        try {

            Map<String, Object> request = new HashMap<>();
            request.put("userId", userId);
            List<MLMovieDTO> mlMovies=movies.stream()
                            .map(m->new MLMovieDTO(m.getId(), m.getGenre()))
                            .toList();
            List<MLInteractionDTO> mlInteractions = allInteractions.stream()
                    .map(i -> new MLInteractionDTO(
                            i.getUserId(),
                            i.getMovieId(),
                            i.getScore(),
                            i.getTimestamp()
                    ))
                    .toList();
            request.put("movies", mlMovies);
            request.put("interactions", mlInteractions);
            long start=System.currentTimeMillis();
            System.out.println("ML URL = " + url);
            System.out.println("CALLING ML SERVICE");
            List<Map<String, Object>> response =
                    restTemplate.postForObject(url, request, List.class);
            long end=System.currentTimeMillis();
            log.info("ML call latency: {} ms", (end-start));
            if(response==null) return Collections.emptyList();

            return response.stream()
                    .map(item -> {
                        Object id = item.get("movieId");
                        if (id == null) return null;
                        return ((Number) id).longValue();
                    })
                    .filter(Objects::nonNull)
                    .toList();


//        }catch (ResourceAccessException e) {
//            log.warn("⚠️ ML timeout (connect/read): {}", e.getMessage());
//            return Collections.emptyList();
//        }
//        catch (HttpStatusCodeException e) {
//            log.error("❌ ML returned error response: {}", e.getStatusCode());
//            return Collections.emptyList();
        }
        catch (Exception e) {
            log.error("❌ ML unexpected failure: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
    public List<Long> fallbackRecommendations(
            Long userId,
            List<Movie> movies,
            List<Interaction> allInteractions,
            Throwable t
    ) {
        log.warn("⚠️ ML FAILED → Using fallback for user {}", userId);

        return getPopularMovies();
    }
    private List<Long> getPopularMovies() {
        return movieRepository.findAll()
                .stream()
                .sorted((a, b) -> Double.compare(
                        b.getPopularityScore(),
                        a.getPopularityScore()))
                .limit(10)
                .map(movie -> movie.getId())
                .toList();
    }

}
