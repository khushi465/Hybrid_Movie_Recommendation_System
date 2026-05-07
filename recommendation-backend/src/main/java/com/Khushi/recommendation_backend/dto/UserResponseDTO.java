package com.Khushi.recommendation_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserResponseDTO implements Serializable {
    private static final long serialVersionUID=1L;
    private Long id;
    private String name;
    private String email;
    private int interactionCount;

}
