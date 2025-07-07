package com.example.moviewatchlist.repository;

import com.example.moviewatchlist.model.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("MovieRepository")
class MovieRepositoryTest {

  @Autowired
  MovieRepository repo;

  @Test
  @DisplayName("persists and retrieves a movie entity")
  void saveAndFind() {
    // Arrange
    Movie inception = Movie.builder().title("Inception").imdbId("tt1375666").build();

    // Act
    Movie saved = repo.save(inception);

    // Assert
    assertThat(saved.getId()).isNotNull();
    assertThat(repo.findById(saved.getId())).hasValueSatisfying(
        m -> assertThat(m.getTitle()).isEqualTo("Inception"));
  }
}
