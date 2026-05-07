package com.Khushi.recommendation_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class InteractionRequestDTO {
    @NotNull(message="UserId is required")
    private Long userId;
    @NotNull(message="MovieId is required")
    private Long movieId;
    @NotNull(message="Type is required")
    private String type;
    @NotNull(message="Score is required")
    @Min(value=0, message="Score must be >=0")
    @Max(value=10, message="Score must be <=0")
    private Double score;
}
