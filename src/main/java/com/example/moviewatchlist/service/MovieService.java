package com.example.moviewatchlist.service;

import com.example.moviewatchlist.client.OmdbApiClient;
import com.example.moviewatchlist.client.TmdbApiClient;
import com.example.moviewatchlist.config.TmdbApiConfig;
import com.example.moviewatchlist.dto.MovieDataDto;
import com.example.moviewatchlist.dto.omdb.OmdbMovieDto;
import com.example.moviewatchlist.dto.tmdb.TmdbImageDto;
import com.example.moviewatchlist.dto.tmdb.TmdbMovieDto;
import com.example.moviewatchlist.dto.tmdb.TmdbMovieSearchResponseDto;
import com.example.moviewatchlist.model.Movie;
import com.example.moviewatchlist.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.moviewatchlist.dto.CombinedMovieDto;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final OmdbApiClient omdbApiClient;
    private final TmdbApiClient tmdbApiClient;
    private final TmdbApiConfig tmdbApiConfig;

    @Autowired
    public MovieService(MovieRepository movieRepository,
            OmdbApiClient omdbApiClient,
            TmdbApiClient tmdbApiClient,
            TmdbApiConfig tmdbApiConfig) {
        this.movieRepository = movieRepository;
        this.omdbApiClient = omdbApiClient;
        this.tmdbApiClient = tmdbApiClient;
        this.tmdbApiConfig = tmdbApiConfig;
    }

    public List<MovieDataDto> lookupByTitle(String title) {
        OmdbMovieDto omdb = omdbApiClient.getMovieData(title);
        TmdbMovieSearchResponseDto search = tmdbApiClient.searchMovie(title);
        TmdbMovieDto tmdb = null;
        if (search != null && !search.getResults().isEmpty()) {
            tmdb = tmdbApiClient.getMovieDetails(search.getResults().get(0).getId());
        }
        MovieDataDto o = new MovieDataDto(
                "omdb",
                omdb.getImdbId(),
                tmdb != null ? tmdb.getId() : null,
                omdb.getTitle(),
                omdb.getYear(),
                omdb.getDirector(),
                omdb.getGenre(),
                omdb.getPoster(),
                tmdb != null ? tmdb.getReleaseDate() : null,
                tmdb != null && tmdb.getImages() != null && !tmdb.getImages().getPosters().isEmpty()
                        ? tmdb.getImages().getPosters().get(0).getVoteAverage()
                        : null,
                tmdb != null && tmdb.getImages() != null && !tmdb.getImages().getPosters().isEmpty()
                        ? tmdb.getImages().getPosters().get(0).getVoteCount()
                        : null);
        MovieDataDto t = new MovieDataDto(
                "tmdb",
                omdb.getImdbId(),
                tmdb != null ? tmdb.getId() : null,
                tmdb != null ? tmdb.getTitle() : omdb.getTitle(),
                null,
                null,
                null,
                tmdb != null && tmdb.getPosterPath() != null
                        ? tmdbApiConfig.getImageBaseUrl() + tmdb.getPosterPath()
                        : null,
                tmdb != null ? tmdb.getReleaseDate() : null,
                tmdb != null && tmdb.getImages() != null && !tmdb.getImages().getPosters().isEmpty()
                        ? tmdb.getImages().getPosters().get(0).getVoteAverage()
                        : null,
                tmdb != null && tmdb.getImages() != null && !tmdb.getImages().getPosters().isEmpty()
                        ? tmdb.getImages().getPosters().get(0).getVoteCount()
                        : null);
        return List.of(o, t);
    }

    public List<MovieDataDto> fetchSources(Movie movie) {
        OmdbMovieDto omdb = omdbApiClient.getMovieById(movie.getImdbId());
        TmdbMovieSearchResponseDto search = tmdbApiClient.searchMovie(movie.getTitle());
        TmdbMovieDto tmdb = null;
        if (search != null && !search.getResults().isEmpty()) {
            Long id = search.getResults().get(0).getId();
            tmdb = tmdbApiClient.getMovieDetails(id);
        }
        MovieDataDto o = new MovieDataDto(
                "omdb",
                omdb.getImdbId(),
                null,
                omdb.getTitle(),
                omdb.getYear(),
                omdb.getDirector(),
                omdb.getGenre(),
                omdb.getPoster(),
                null,
                null,
                null);
        MovieDataDto t = new MovieDataDto(
                "tmdb",
                movie.getImdbId(),
                tmdb != null ? tmdb.getId() : null,
                tmdb != null ? tmdb.getTitle() : movie.getTitle(),
                null,
                null,
                null,
                tmdb != null && tmdb.getPosterPath() != null
                        ? tmdbApiConfig.getImageBaseUrl() + tmdb.getPosterPath()
                        : null,
                tmdb != null ? tmdb.getReleaseDate() : null,
                tmdb != null && tmdb.getImages() != null && !tmdb.getImages().getPosters().isEmpty()
                        ? tmdb.getImages().getPosters().get(0).getVoteAverage()
                        : null,
                tmdb != null && tmdb.getImages() != null && !tmdb.getImages().getPosters().isEmpty()
                        ? tmdb.getImages().getPosters().get(0).getVoteCount()
                        : null);
        return List.of(o, t);
    }

    public CombinedMovieDto fetchCombined(Movie movie) {
        OmdbMovieDto omdb = omdbApiClient.getMovieById(movie.getImdbId());

        TmdbMovieSearchResponseDto search = tmdbApiClient.searchMovie(movie.getTitle());
        TmdbMovieDto tmdb = null;
        if (search != null && !search.getResults().isEmpty()) {
            Long id = search.getResults().get(0).getId();
            tmdb = tmdbApiClient.getMovieDetails(id);
        }
        return new CombinedMovieDto(omdb, tmdb);
    }

    public Movie addMovieByTitle(String title) {
        OmdbMovieDto omdbMovie = omdbApiClient.getMovieData(title);
        if (omdbMovie == null || !"True".equalsIgnoreCase(omdbMovie.getResponse()))
            return null;
        TmdbMovieSearchResponseDto tmdbSearch = tmdbApiClient.searchMovie(title);
        Long tmdbId = (tmdbSearch != null && !tmdbSearch.getResults().isEmpty())
                ? tmdbSearch.getResults().get(0).getId()
                : null;
        TmdbMovieDto tmdbDetails = (tmdbId != null) ? tmdbApiClient.getMovieDetails(tmdbId) : null;
        Movie m = new Movie();
        m.setImdbId(omdbMovie.getImdbId());
        m.setTitle(omdbMovie.getTitle());
        m.setYear(omdbMovie.getYear());
        m.setDirector(omdbMovie.getDirector());
        m.setGenre(omdbMovie.getGenre());
        if (tmdbDetails != null) {
            if (tmdbDetails.getSimilar() != null && tmdbDetails.getSimilar().getResults() != null) {
                String sim = tmdbDetails.getSimilar().getResults().stream()
                        .limit(5)
                        .map(TmdbMovieDto::getTitle)
                        .collect(Collectors.joining(", "));
                m.setSimilarMovies(sim);
            }
            m.setImageFilePath(
                    tmdbDetails.getPosterPath() != null
                            ? tmdbApiConfig.getImageBaseUrl() + tmdbDetails.getPosterPath()
                            : omdbMovie.getPoster());
        } else {
            m.setImageFilePath(omdbMovie.getPoster());
        }
        m.setWatched(false);
        m.setRating(null);
        return movieRepository.save(m);
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie updateWatched(Long id, boolean watched) {
        Optional<Movie> o = movieRepository.findById(id);
        if (o.isEmpty())
            return null;
        Movie m = o.get();
        m.setWatched(watched);
        return movieRepository.save(m);
    }

    public Movie updateRating(Long id, Integer rating) {
        Optional<Movie> o = movieRepository.findById(id);
        if (o.isEmpty())
            return null;
        Movie m = o.get();
        m.setRating(rating);
        return movieRepository.save(m);
    }

    public boolean deleteMovie(Long id) {
        if (!movieRepository.existsById(id))
            return false;
        movieRepository.deleteById(id);
        return true;
    }
}
