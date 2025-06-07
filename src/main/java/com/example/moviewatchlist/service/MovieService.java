package main.java.com.example.moviewatchlist.service;

import main.java.com.example.moviewatchlist.model.Movie;
import main.java.com.example.moviewatchlist.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring service component
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired // Spring injects MovieRepository here
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // We will add more methods here later for fetching from APIs,
    // downloading images, retrieving lists, updating, and deleting.
}