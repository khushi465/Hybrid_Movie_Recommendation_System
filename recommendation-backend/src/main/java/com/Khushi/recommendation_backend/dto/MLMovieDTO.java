package com.Khushi.recommendation_backend.dto;

public class MLMovieDTO {
    private Long id;
    private String genre;
    public MLMovieDTO(Long id, String genre){
        this.id=id;
        this.genre=genre;
    }
    public Long getId(){return id;}
    public String getGenre(){ return genre;}
}
