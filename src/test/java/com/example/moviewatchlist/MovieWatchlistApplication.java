// Adjust package declaration based on your convention
package com.example.moviewatchlist;

import com.example.moviewatchlist.model.Movie;
import com.example.moviewatchlist.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class MovieWatchlistApplication {

    // Define a logger for this class
    private static final Logger log = LoggerFactory.getLogger(MovieWatchlistApplication.class);

    @Autowired
    private MovieService movieService;

    public static void main(String[] args) {
        SpringApplication.run(MovieWatchlistApplication.class, args);
    }

    /**
     * TODO: Remove this temporary test method later.
     * This method runs automatically after the Spring Boot application starts.
     * It's used here for a quick functional test of the MovieService and API integrations.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        log.info("--- Application Ready! Running a quick test ---");

        String movieTitleToSearch = "Inception";
        try {
            log.info("Attempting to add movie: {}", movieTitleToSearch);
            Movie addedMovie = movieService.addMovieByTitle(movieTitleToSearch);

            if (addedMovie != null) {
                log.info("Successfully added movie to watchlist:");
                log.info("  ID: {}", addedMovie.getId());
                log.info("  Title: {}", addedMovie.getTitle());
                log.info("  IMDb ID: {}", addedMovie.getImdbId());
                log.info("  Director: {}", addedMovie.getDirector());
                log.info("  Poster Path (OMDb URL): {}", addedMovie.getImageFilePath());
                log.info("  Watched: {}", addedMovie.isWatched());
                log.info("  Rating: {}", addedMovie.getRating());
            } else {
                log.error("Failed to add movie: {}. Check logs for details.", movieTitleToSearch);
            }
        } catch (Exception e) {
            log.error("An error occurred during movie addition: {}", e.getMessage(), e);
        }

        log.info("--- Quick test finished ---");
    }
}