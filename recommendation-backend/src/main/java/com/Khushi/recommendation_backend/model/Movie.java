package com.Khushi.recommendation_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name="movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private String tags;

    //Ranking sigals
    private double popularityScore;
}
