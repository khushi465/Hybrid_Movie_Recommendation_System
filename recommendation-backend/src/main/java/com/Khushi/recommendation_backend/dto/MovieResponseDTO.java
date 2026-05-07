package com.Khushi.recommendation_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MovieResponseDTO implements Serializable {
    private static final long serialVersionUID=1L;
    private Long id;
    private String title;
    private String genre;
    private String tags;
    private double popularityScore;
}
