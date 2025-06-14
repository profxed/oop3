package com.example.moviewatchlist.dto.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OmdbMovieDto {
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("Director")
    private String director;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("imdbID")
    private String imdbId;
    @JsonProperty("Poster")
    private String poster; // URL to the poster
    @JsonProperty("Response")
    private String response; // "True" or "False" indicating success
    @JsonProperty("Error")
    private String error; // Error message if Response is "False"
}