package com.Khushi.recommendation_backend.controller;

import com.Khushi.recommendation_backend.dto.MovieResponseDTO;
import com.Khushi.recommendation_backend.model.Movie;
import com.Khushi.recommendation_backend.service.RecommendationService;
import com.Khushi.recommendation_backend.util.CacheMetrics;
import com.Khushi.recommendation_backend.util.LatencyTracker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private static final Logger log= LoggerFactory.getLogger(RecommendationController.class);
    private final RecommendationService recommendationService;
    @GetMapping("/debug/{userId}")
    public List<MovieResponseDTO> getRecommendations(@PathVariable("userId") Long userId){
        long start=System.currentTimeMillis();
        List<MovieResponseDTO> result= recommendationService.getRecommendations(userId,false);
        long end=System.currentTimeMillis();
        long latency=end-start;
        LatencyTracker.record(latency);
        if(latency < 20){
            CacheMetrics.recordHit();
            log.info("CACHE HIT → {} ms", latency);
        } else {
            CacheMetrics.recordMiss();
            log.info("CACHE MISS → {} ms", latency);
        }
        return result;
    }
//    @CachePut(value="recommendations", key="'user:' + #userId")
//    public List<MovieResponseDTO> refreshRecommendations(Long userId) {
//        return getRecommendations(userId);
//    }
}
