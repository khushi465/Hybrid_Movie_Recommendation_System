package com.Khushi.recommendation_backend.controller;

import com.Khushi.recommendation_backend.dto.MovieResponseDTO;
import com.Khushi.recommendation_backend.repository.InteractionRepository;
import com.Khushi.recommendation_backend.service.RecommendationService;
import com.Khushi.recommendation_backend.util.CacheMetrics;
import com.Khushi.recommendation_backend.util.LatencyTracker;
import com.Khushi.recommendation_backend.util.RecommendationMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/metrics")
public class MetricsController {
    private final RecommendationService recommendationService;
    private final InteractionRepository interactionRepository;
    @GetMapping("/latency")
    public Map<String, Object> getLatencyMetrics(){
        Map<String, Object> map=new HashMap<>();
        map.put("requests", LatencyTracker.size());
        map.put("p50_ms", LatencyTracker.getP50());
        map.put("p95_ms", LatencyTracker.getP95());
        return map;
    }
    @GetMapping("/cache")
    public Map<String, Object> cache() {
        Map<String, Object> map = new HashMap<>();

        map.put("p50", LatencyTracker.getP50());
        map.put("p95", LatencyTracker.getP95());
        map.put("hit_rate", CacheMetrics.getHitRate());

        return map;
    }
    @GetMapping("/performance")
    public Map<String, Object> performance() {
        Map<String, Object> map = new HashMap<>();
        double p50=LatencyTracker.getP50();
        double p95=LatencyTracker.getP95();
        double improvement=((p95-p50)/p95)*100;
        map.put("p50", p50);
        map.put("p95", p95);
        map.put("improvement_percent", improvement);

        return map;
    }

    @GetMapping("/evaluate/{userId}")
    public Map<String, Object> evaluate(@PathVariable Long userId) {

        List<Long> allInteractions = interactionRepository.findByUserId(userId)
                .stream()
                .map(i -> i.getMovieId())
                .toList();

        if (allInteractions.size() < 5) {
            return Map.of("error", "Not enough data for evaluation");
        }

        int split = (int)(allInteractions.size() * 0.8);

        List<Long> train = allInteractions.subList(0, split);
        List<Long> test = allInteractions.subList(split, allInteractions.size());

        List<MovieResponseDTO> recs = recommendationService.getRecommendations(userId,true);

        List<Long> recommendedIds = recs.stream()
                .map(MovieResponseDTO::getId)
                .toList();

        double hitRate = RecommendationMetrics.hitRate(recommendedIds, test);
        double precision = RecommendationMetrics.precisionAtK(recommendedIds, test);

        Map<String, Object> result = new HashMap<>();
        result.put("hit_rate", hitRate);
        result.put("precision_at_k", precision);
        result.put("train_size", train.size());
        result.put("test_size", test.size());

        return result;
    }
}
