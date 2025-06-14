package com.example.moviewatchlist.service;

import com.example.moviewatchlist.model.Movie;
import com.example.moviewatchlist.repository.MovieRepository;
import com.example.moviewatchlist.client.OmdbApiClient;
import com.example.moviewatchlist.client.TmdbApiClient;
import com.example.moviewatchlist.dto.omdb.OmdbMovieDto;
import com.example.moviewatchlist.dto.tmdb.TmdbMovieSearchResponseDto;
import com.example.moviewatchlist.dto.tmdb.TmdbMovieDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final OmdbApiClient omdbApiClient;
    private final TmdbApiClient tmdbApiClient;

    @Autowired
    public MovieService(MovieRepository movieRepository, OmdbApiClient omdbApiClient, TmdbApiClient tmdbApiClient) {
        this.movieRepository = movieRepository;
        this.omdbApiClient = omdbApiClient;
        this.tmdbApiClient = tmdbApiClient;
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie addMovieByTitle(String title) {
        OmdbMovieDto omdbMovie = omdbApiClient.getMovieData(title);

        if (omdbMovie == null || !"True".equalsIgnoreCase(omdbMovie.getResponse())) {
            // TODO: Handle movie not found or errors from OMDb API gracefully
            System.err.println("OMDb API Error: " + (omdbMovie != null ? omdbMovie.getError() : "Unknown error"));
            
            // Possibly throw a custom exception?
            return null;
        }

        TmdbMovieSearchResponseDto tmdbSearchResponse = tmdbApiClient.searchMovie(title);
        Long tmdbMovieId = null;
        if (tmdbSearchResponse != null && tmdbSearchResponse.getResults() != null && !tmdbSearchResponse.getResults().isEmpty()) {
            
            // Get the first result, assuming it's the most relevant
            tmdbMovieId = tmdbSearchResponse.getResults().get(0).getId();
        }

        TmdbMovieDto tmdbMovieDetails = null;
        if (tmdbMovieId != null) {
            tmdbMovieDetails = tmdbApiClient.getMovieDetails(tmdbMovieId);
        } else {
            // TODO: Handle case where movie is found on OMDb but not TMDB search
            System.err.println("Movie found on OMDb but not in TMDB search: " + title);
        }

        // Create a Movie entity
        Movie movie = new Movie();
        movie.setImdbId(omdbMovie.getImdbId());
        movie.setTitle(omdbMovie.getTitle());
        movie.setYear(omdbMovie.getYear());
        movie.setDirector(omdbMovie.getDirector());
        movie.setGenre(omdbMovie.getGenre());

        // TODO: Extract similar movies from tmdbMovieDetails and set it.
        // This requires parsing the nested JSON structure for 'similar' movies from TmdbMovieDto
        if (tmdbMovieDetails != null && tmdbMovieDetails.getSimilar() != null) {
            // Assuming getSimilar() exists in TmdbMovieDto
            // We'll need to update TmdbMovieDto to include similar movies
        }

        // TODO: Implement image downloading and set imageFilePath
        // For now, we can just store the poster URL as a placeholder or null.
        movie.setImageFilePath(omdbMovie.getPoster());

        // Initial values for watched and rating
        movie.setWatched(false);
        movie.setRating(null);

        // TODO: The database...
        // Save the movie to the database when available
        return movieRepository.save(movie);
    }

    // TODO: Add methods for retrieving, updating watched status, updating rating, and deleting movies.
}