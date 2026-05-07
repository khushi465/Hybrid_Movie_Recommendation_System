package com.Khushi.recommendation_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovieRequestDTO {
    @NotNull(message="Title is required")
    private String title;
    @NotNull(message="Genre is required")
    private String genre;
    private String tags;
    private double popularityScore;
}
