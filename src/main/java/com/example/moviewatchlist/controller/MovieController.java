// src/main/java/com/example/moviewatchlist/controller/MovieController.java
package com.example.moviewatchlist.controller;

import com.example.moviewatchlist.dto.MovieDataDto;
import com.example.moviewatchlist.model.Movie;
import com.example.moviewatchlist.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    /** 
     * @param pageable
     * @return Page<List<MovieDataDto>>
     */
    @GetMapping
    public Page<List<MovieDataDto>> getAll(Pageable pageable) {
        return movieService.getAllMovies(pageable)
                .map(movieService::fetchSources);
    }

    /** 
     * @param title
     * @return ResponseEntity<List<MovieDataDto>>
     */
    @GetMapping("/search")
    public ResponseEntity<List<MovieDataDto>> search(@RequestParam String title) {
        List<MovieDataDto> result = movieService.lookupByTitle(title);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /** 
     * @param title
     * @return ResponseEntity<List<MovieDataDto>>
     */
    @PostMapping
    public ResponseEntity<List<MovieDataDto>> addAndFetch(@RequestParam String title) {
        Movie saved = movieService.addMovieByTitle(title);
        if (saved == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(movieService.fetchSources(saved));
    }

    /** 
     * @param id
     * @param watched
     * @return ResponseEntity<Movie>
     */
    @PatchMapping("/{id}/watched")
    public ResponseEntity<Movie> updateWatched(@PathVariable Long id, @RequestParam boolean watched) {
        Movie m = movieService.updateWatched(id, watched);
        return m != null ? ResponseEntity.ok(m) : ResponseEntity.notFound().build();
    }

    /** 
     * @param id
     * @param rating
     * @return ResponseEntity<Movie>
     */
    @PatchMapping("/{id}/rating")
    public ResponseEntity<Movie> updateRating(@PathVariable Long id, @RequestParam int rating) {
        Movie m = movieService.updateRating(id, rating);
        return m != null ? ResponseEntity.ok(m) : ResponseEntity.notFound().build();
    }

    /** 
     * @param id
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return movieService.deleteMovie(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
