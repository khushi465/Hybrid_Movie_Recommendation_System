package com.Khushi.recommendation_backend.repository;

import com.Khushi.recommendation_backend.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    //for fallback recommendations
    List<Movie> findTop10ByOrderByPopularityScoreDesc();
}
