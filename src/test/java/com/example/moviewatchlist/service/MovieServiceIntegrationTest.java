package com.example.moviewatchlist.service;

import com.example.moviewatchlist.model.Movie;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class MovieServiceIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(MovieServiceIntegrationTest.class);

    @Autowired
    private MovieService movieService;

    @Test
    public void testAddMovieByTitle() {
        log.info("--- Running integration test for adding a movie ---");
        String movieTitleToSearch = "Inception";
        try {
            log.info("Attempting to add movie: {}", movieTitleToSearch);
            Movie addedMovie = movieService.addMovieByTitle(movieTitleToSearch);

            assertNotNull(addedMovie, "The added movie should not be null.");
            assertNotNull(addedMovie.getId(), "The movie should have an ID after being saved.");

            log.info("Successfully added movie to watchlist:");
            log.info("  ID: {}", addedMovie.getId());
            log.info("  Title: {}", addedMovie.getTitle());
            log.info("  IMDb ID: {}", addedMovie.getImdbId());
            log.info("  Director: {}", addedMovie.getDirector());
            log.info("  Poster Path (OMDb URL): {}", addedMovie.getImageFilePath());
            log.info("  Watched: {}", addedMovie.isWatched());
            log.info("  Rating: {}", addedMovie.getRating());

        } catch (Exception e) {
            log.error("An error occurred during the movie addition test: {}", e.getMessage(), e);
        }
        log.info("--- Integration test finished ---");
    }
}