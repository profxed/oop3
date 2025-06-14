package com.example.moviewatchlist.dto.tmdb;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbImagesResponseDto {
    private int id; // Movie ID
    private List<TmdbImageDto> backdrops;
    private List<TmdbImageDto> posters;
}