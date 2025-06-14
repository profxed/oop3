package com.example.moviewatchlist.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbImageDto {
    @JsonProperty("aspect_ratio")
    private double aspectRatio;
    @JsonProperty("file_path")
    private String filePath; // The relative path to the image file
    private int height;
    @JsonProperty("iso_639_1")
    private String language; // e.g., "en" for English
    @JsonProperty("vote_average")
    private double voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;
    private int width;
}