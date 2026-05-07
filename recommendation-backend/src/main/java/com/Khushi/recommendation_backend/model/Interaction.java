package com.Khushi.recommendation_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="interactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interaction implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long movieId;

    private String type; //click, like, watch
    private double score; //weight of interaction for ranking
    private LocalDateTime timestamp;

}
