package com.example.moviewatchlist.service;

import com.example.moviewatchlist.model.Movie;
import com.example.moviewatchlist.repository.MovieRepository;
import com.example.moviewatchlist.client.OmdbApiClient;
import com.example.moviewatchlist.client.TmdbApiClient;
import com.example.moviewatchlist.dto.omdb.OmdbMovieDto;
import com.example.moviewatchlist.dto.tmdb.TmdbMovieSearchResponseDto;
import com.example.moviewatchlist.dto.tmdb.TmdbMovieDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Movie addMovieByTitle(String title) {
        OmdbMovieDto omdbMovie = omdbApiClient.getMovieData(title);
        if (omdbMovie == null || !"True".equalsIgnoreCase(omdbMovie.getResponse())) return null;

        TmdbMovieSearchResponseDto tmdbSearch = tmdbApiClient.searchMovie(title);
        Long tmdbId = (tmdbSearch != null && !tmdbSearch.getResults().isEmpty()) ? tmdbSearch.getResults().get(0).getId() : null;
        TmdbMovieDto tmdbDetails = (tmdbId != null) ? tmdbApiClient.getMovieDetails(tmdbId) : null;

        Movie movie = new Movie();
        movie.setImdbId(omdbMovie.getImdbId());
        movie.setTitle(omdbMovie.getTitle());
        movie.setYear(omdbMovie.getYear());
        movie.setDirector(omdbMovie.getDirector());
        movie.setGenre(omdbMovie.getGenre());
        movie.setImageFilePath(omdbMovie.getPoster());
        movie.setWatched(false);
        movie.setRating(null);

        return movieRepository.save(movie);
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie updateWatched(Long id, boolean watched) {
        Optional<Movie> optional = movieRepository.findById(id);
        if (optional.isEmpty()) return null;
        Movie movie = optional.get();
        movie.setWatched(watched);
        return movieRepository.save(movie);
    }

    public Movie updateRating(Long id, Integer rating) {
        Optional<Movie> optional = movieRepository.findById(id);
        if (optional.isEmpty()) return null;
        Movie movie = optional.get();
        movie.setRating(rating);
        return movieRepository.save(movie);
    }

    public boolean deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) return false;
        movieRepository.deleteById(id);
        return true;
    }
}
