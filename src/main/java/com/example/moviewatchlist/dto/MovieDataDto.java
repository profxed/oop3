package com.example.moviewatchlist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieDataDto {
    private String source;
    private String imdbId;
    private Long tmdbId;
    private String title;
    private String year;
    private String director;
    private String genre;
    private String poster;
    private String releaseDate;
    private Double voteAverage;
    private Integer voteCount;
}
