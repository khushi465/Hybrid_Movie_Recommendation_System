package com.Khushi.recommendation_backend.util;

import java.util.List;

public class RecommendationMetrics {

    // Hit Rate @ K
    public static double hitRate(List<Long> recommended, List<Long> actual) {
        for (Long r : recommended) {
            if (actual.contains(r)) return 1.0;
        }
        return 0.0;
    }

    // Precision @ K
    public static double precisionAtK(List<Long> recommended, List<Long> actual) {
        if (recommended == null || recommended.isEmpty()) return 0;

        int hits = 0;
        for (Long r : recommended) {
            if (actual.contains(r)) hits++;
        }

        return (double) hits / recommended.size();
    }
}