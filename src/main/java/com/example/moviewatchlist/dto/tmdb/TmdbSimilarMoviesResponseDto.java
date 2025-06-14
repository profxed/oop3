package com.example.moviewatchlist.dto.tmdb;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbSimilarMoviesResponseDto {
    private int page;
    private List<TmdbMovieDto> results; // List of similar movies
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_results")
    private int totalResults;
}