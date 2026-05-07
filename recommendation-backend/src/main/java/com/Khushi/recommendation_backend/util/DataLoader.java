package com.Khushi.recommendation_backend.util;

import com.Khushi.recommendation_backend.model.Movie;
import com.Khushi.recommendation_backend.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;

@Component
@RequiredArgsConstructor
public class DataLoader {
    private final MovieRepository movieRepository;
    @PostConstruct
    public void loadMovies(){
        if(movieRepository.count() > 0) {
            System.out.println("Movies already loaded");
            return;
        }
        try(BufferedReader br=new BufferedReader(new FileReader("movies.csv"))){
            String line;
            br.readLine();//skip header
            while((line=br.readLine())!=null){
                String[] data=line.split(",",3);//movie titles contain commas

                Movie movie=new Movie();
                movie.setTitle(data[1]);
                movie.setGenre(data[2]);

                movieRepository.save(movie);
            }
            System.out.println("Movies loaded");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void loadRatings(){
        try(BufferedReader br=new BufferedReader(new FileReader("ratings.csv"))){
            String line;
            br.readLine();
            while((line=br.readLine())!=null){
                String[] data=line.split(",");
                Long userId=Long.parseLong(data[0]);
                Long movieId=Long.parseLong(data[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
