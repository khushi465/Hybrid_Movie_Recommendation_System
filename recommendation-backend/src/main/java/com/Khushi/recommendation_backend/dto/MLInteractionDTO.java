package com.Khushi.recommendation_backend.dto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MLInteractionDTO {
    private Long userId;
    private Long movieId;
    private Double score;
    private Long timestamp;

    public MLInteractionDTO(Long userId, Long movieId, Double score, LocalDateTime timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.score = score;
        this.timestamp = timestamp.toEpochSecond(ZoneOffset.UTC);
    }

    public Long getUserId() { return userId; }
    public Long getMovieId() { return movieId; }
    public Double getScore() { return score; }
    public Long getTimestamp() { return timestamp; }
}
