package com.example.moviewatchlist.client;

import com.example.moviewatchlist.config.TmdbApiConfig;
import com.example.moviewatchlist.dto.tmdb.TmdbMovieSearchResponseDto;
import com.example.moviewatchlist.dto.tmdb.TmdbMovieDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class TmdbApiClient {

    private final RestTemplate restTemplate;
    private final TmdbApiConfig tmdbApiConfig;

    @Autowired
    public TmdbApiClient(RestTemplateBuilder restTemplateBuilder, TmdbApiConfig tmdbApiConfig) {
        this.restTemplate = restTemplateBuilder.build();
        this.tmdbApiConfig = tmdbApiConfig;
    }

    public TmdbMovieSearchResponseDto searchMovie(String title) {
        String url = UriComponentsBuilder.fromUriString(tmdbApiConfig.getBaseUrl() + "search/movie")
                .queryParam("api_key", tmdbApiConfig.getKey())
                .queryParam("query", title)
                .build().toUriString();
        return restTemplate.getForObject(url, TmdbMovieSearchResponseDto.class);
    }

    public TmdbMovieDto getMovieDetails(Long movieId) {
        String url = UriComponentsBuilder.fromUriString(tmdbApiConfig.getBaseUrl() + "movie/" + movieId)
                .queryParam("api_key", tmdbApiConfig.getKey())
                .queryParam("append_to_response", "images,similar") // Request images and similar movies
                .build().toUriString();
        return restTemplate.getForObject(url, TmdbMovieDto.class);
    }

    // TODO: Add a method to fetch images specifically if needed, or parse from TmdbMovieDto later.
    // The getMovieDetails already fetches 'images' and 'similar' if append_to_response is used.
    // we'll need to parse the TmdbMovieDto for these appended responses, handled in the MovieService.
}