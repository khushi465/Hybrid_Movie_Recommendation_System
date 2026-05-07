package com.Khushi.recommendation_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true, nullable=false)
    private String email;

    //for dynamic weighting
    private int interactionCount;
}
