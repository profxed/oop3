package com.example.moviewatchlist.controller;

import com.example.moviewatchlist.dto.MovieDataDto;
import com.example.moviewatchlist.model.Movie;
import com.example.moviewatchlist.service.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@ActiveProfiles("test")
class MovieControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private MovieService movieService;

  /* --------------------------------------------------------------------- */
  @Nested
  @DisplayName("GET /movies/search")
  class Search {

    @Test
    @DisplayName("returns JSON array when results are found")
    void search_returnsJsonArray_whenResultsFound() throws Exception {
      // Arrange
      List<MovieDataDto> payload = List.of(
          new MovieDataDto("omdb", "tt1375666", null, "Inception",
              "2010", "Christopher Nolan", "Action", "/poster.jpg",
              null, null, null));

      given(movieService.lookupByTitle("Inception")).willReturn(payload);

      // Act & Assert
      mockMvc.perform(get("/movies/search").param("title", "Inception"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].title").value("Inception"));
    }
  }

  /* --------------------------------------------------------------------- */
  @Nested
  @DisplayName("POST /movies")
  class AddAndFetch {

    @Test
    @DisplayName("persists movie and returns combined sources")
    void addAndFetch_returnsCombinedSources_afterPersisting() throws Exception {
      // Arrange
      Movie saved = Movie.builder().id(1L).imdbId("tt1375666").title("Inception").build();
      List<MovieDataDto> sources = List.of(
          new MovieDataDto("omdb", "tt1375666", null, "Inception",
              "2010", "Christopher Nolan", "Action", "/omdbPoster.jpg",
              null, null, null),
          new MovieDataDto("tmdb", "tt1375666", 27205L, "Inception",
              null, null, null, "/tmdbPoster.jpg",
              "2010-07-16", 8.8, 2000));

      given(movieService.addMovieByTitle("Inception")).willReturn(saved);
      given(movieService.fetchSources(saved)).willReturn(sources);

      // Act & Assert
      mockMvc.perform(post("/movies").param("title", "Inception"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(2)))
          .andExpect(jsonPath("$[1].source").value("tmdb"));
    }
  }

  /* --------------------------------------------------------------------- */
  @Nested
  @DisplayName("PATCH /movies/{id}")
  class Patching {

    @Test
    @DisplayName("updates watched flag")
    void updateWatched_returnsUpdatedEntity() throws Exception {
      // Arrange
      Movie updated = Movie.builder().id(9L).title("Interstellar").watched(true).build();
      given(movieService.updateWatched(9L, true)).willReturn(updated);

      // Act & Assert
      mockMvc.perform(patch("/movies/{id}/watched", 9).param("watched", "true"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.watched").value(true));
    }

    @Test
    @DisplayName("updates rating")
    void updateRating_returnsUpdatedEntity() throws Exception {
      // Arrange
      Movie updated = Movie.builder().id(9L).title("Interstellar").rating(5).build();
      given(movieService.updateRating(9L, 5)).willReturn(updated);

      // Act & Assert
      mockMvc.perform(patch("/movies/{id}/rating", 9).param("rating", "5"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.rating").value(5));
    }
  }

  /* --------------------------------------------------------------------- */
  @Nested
  @DisplayName("DELETE /movies/{id}")
  class Deleting {

    @Test
    @DisplayName("returns 204 when delete succeeds")
    void delete_returnsNoContent_whenSuccessful() throws Exception {
      given(movieService.deleteMovie(4L)).willReturn(true);

      mockMvc.perform(delete("/movies/{id}", 4))
          .andExpect(status().isNoContent());
    }
  }
}
