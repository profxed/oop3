package com.example.moviewatchlist.service;

import com.example.moviewatchlist.client.OmdbApiClient;
import com.example.moviewatchlist.client.TmdbApiClient;
import com.example.moviewatchlist.config.TmdbApiConfig;
import com.example.moviewatchlist.dto.omdb.OmdbMovieDto;
import com.example.moviewatchlist.dto.tmdb.*;
import com.example.moviewatchlist.model.Movie;
import com.example.moviewatchlist.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MovieService unit tests")
class MovieServiceTest {

  @Mock
  private MovieRepository movieRepository;
  @Mock
  private OmdbApiClient omdbApiClient;
  @Mock
  private TmdbApiClient tmdbApiClient;

  @InjectMocks
  private MovieService movieService;
  private TmdbApiConfig tmdbApiConfig;

  @BeforeEach
  void init() {
    tmdbApiConfig = new TmdbApiConfig();
    tmdbApiConfig.setBaseUrl("https://api.themoviedb.org/3/");
    tmdbApiConfig.setImageBaseUrl("https://image.tmdb.org/t/p/w500/");
    tmdbApiConfig.setKey("dummy-key");
    movieService = new MovieService(movieRepository, omdbApiClient, tmdbApiClient, tmdbApiConfig);
  }

  /* ------------------------------------------------------------------- */
  @Nested
  @DisplayName("addMovieByTitle")
  class AddMovie {

    @Test
    @DisplayName("persists a movie when OMDb response is positive")
    void returnsSavedEntity_whenOmdbRespondsTrue() {
      // -------- Arrange --------
      String title = "Inception";
      given(omdbApiClient.getMovieData(title))
          .willReturn(new OmdbMovieDto(
              "Inception", "2010", "Christopher Nolan",
              "Action", "tt1375666", "omdbPoster.jpg",
              "True", null));

      TmdbMovieDto searchHit = new TmdbMovieDto(
          27205L, "Inception", "/poster.jpg",
          "2010-07-16", null, null, null);
      given(tmdbApiClient.searchMovie(title))
          .willReturn(new TmdbMovieSearchResponseDto(1, List.of(searchHit), 1, 1));

      TmdbMovieDto details = new TmdbMovieDto(
          27205L, "Inception", "/poster.jpg", "2010-07-16",
          null,
          new TmdbImagesResponseDto(27205,
              List.of(new TmdbImageDto(1.78, "/back.jpg", 1080, "en", 8.8, 2000, 1920)),
              List.of(new TmdbImageDto(0.67, "/poster.jpg", 800, "en", 8.5, 3000, 540))),
          new TmdbSimilarMoviesResponseDto(1,
              List.of(new TmdbMovieDto(808L, "Interstellar", null, null, null, null, null)),
              1, 1));
      given(tmdbApiClient.getMovieDetails(27205L)).willReturn(details);

      given(movieRepository.save(any(Movie.class)))
          .willAnswer(inv -> {
            Movie m = inv.getArgument(0);
            m.setId(1L);
            return m;
          });

      // -------- Act --------
      Movie saved = movieService.addMovieByTitle(title);

      // -------- Assert --------
      assertThat(saved)
          .isNotNull()
          .extracting(Movie::getId, Movie::getImdbId, Movie::getSimilarMovies)
          .containsExactly(1L, "tt1375666", "Interstellar");
      then(movieRepository).should().save(saved);
    }

    @Test
    @DisplayName("returns null when OMDb cannot find the movie")
    void returnsNull_whenOmdbResponseIsFalse() {
      // Arrange
      given(omdbApiClient.getMovieData("Ghost movie"))
          .willReturn(new OmdbMovieDto(null, null, null, null,
              null, null, "False", "Movie not found!"));

      // Act
      Movie result = movieService.addMovieByTitle("Ghost movie");

      // Assert
      assertThat(result).isNull();
      then(movieRepository).shouldHaveNoInteractions();
    }
  }

  /* ------------------------------------------------------------------- */
  @Nested
  @DisplayName("updateWatched")
  class UpdateWatched {

    @Test
    @DisplayName("sets flag and persists when movie exists")
    void setsFlag_whenMovieExists() {
      // Arrange
      Movie original = Movie.builder().id(42L).title("Inception").watched(false).build();
      given(movieRepository.findById(42L)).willReturn(Optional.of(original));
      given(movieRepository.save(any(Movie.class))).willAnswer(inv -> inv.getArgument(0));

      // Act
      Movie updated = movieService.updateWatched(42L, true);

      // Assert
      assertThat(updated).isNotNull().extracting(Movie::isWatched).isEqualTo(true);
      then(movieRepository).should().save(updated);
    }

    @Test
    @DisplayName("returns null when movie is missing")
    void returnsNull_whenMovieMissing() {
      given(movieRepository.findById(404L)).willReturn(Optional.empty());

      Movie updated = movieService.updateWatched(404L, true);

      assertThat(updated).isNull();
      then(movieRepository).should(never()).save(any());
    }
  }

  /* ------------------------------------------------------------------- */
  @Nested
  @DisplayName("deleteMovie")
  class DeleteMovie {

    @Test
    @DisplayName("deletes entity when present")
    void returnsTrue_whenEntityExists() {
      given(movieRepository.existsById(1L)).willReturn(true);

      boolean result = movieService.deleteMovie(1L);

      assertThat(result).isTrue();
      then(movieRepository).should().deleteById(1L);
    }

    @Test
    @DisplayName("returns false when entity is missing")
    void returnsFalse_whenEntityMissing() {
      given(movieRepository.existsById(99L)).willReturn(false);

      assertThat(movieService.deleteMovie(99L)).isFalse();
      then(movieRepository).shouldHaveNoMoreInteractions();
    }
  }
}
