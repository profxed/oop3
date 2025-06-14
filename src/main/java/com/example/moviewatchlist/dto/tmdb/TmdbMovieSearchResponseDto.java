package com.example.moviewatchlist.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMovieSearchResponseDto {
    private int page;
    private List<TmdbMovieDto> results; // A list of simplified movie DTOs
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_results")
    private int totalResults;
}