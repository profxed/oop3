package com.example.moviewatchlist.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMovieDto {
    private Long id; // TMDB's movie ID
    private String title;
    @JsonProperty("poster_path")
    private String posterPath; // Relative path to poster image
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("backdrop_path")
    private String backdropPath; // Relative path to backdrop image
    @JsonProperty("images")
    private TmdbImagesResponseDto images;
    @JsonProperty("similar")
    private TmdbSimilarMoviesResponseDto similar;
}