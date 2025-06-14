package com.example.moviewatchlist.dto;

import com.example.moviewatchlist.dto.omdb.OmdbMovieDto;
import com.example.moviewatchlist.dto.tmdb.TmdbMovieDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinedMovieDto {
    private OmdbMovieDto omdb;
    private TmdbMovieDto tmdb;
}
