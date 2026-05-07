package com.Khushi.recommendation_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InteractionResponseDTO implements Serializable {
    private static final long serialVersionUID=1L;
    private Long id;
    private Long userId;
    private Long movieId;
    private String type;
    private Double score;
    private LocalDateTime timestamp;
}
