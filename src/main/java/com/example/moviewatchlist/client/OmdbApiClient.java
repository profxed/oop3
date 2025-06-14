package com.example.moviewatchlist.client;

import com.example.moviewatchlist.config.OmdbApiConfig;
import com.example.moviewatchlist.dto.omdb.OmdbMovieDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OmdbApiClient {

    private final RestTemplate restTemplate;
    private final OmdbApiConfig omdbApiConfig;

    @Autowired
    public OmdbApiClient(RestTemplateBuilder restTemplateBuilder, OmdbApiConfig omdbApiConfig) {
        this.restTemplate = restTemplateBuilder.build();
        this.omdbApiConfig = omdbApiConfig;

        System.out.println("OMDB API Key: " + omdbApiConfig.getKey());
    }

    public OmdbMovieDto getMovieData(String title) {
        // UriComponentsBuilder for safe and correct URL construction
        String url = UriComponentsBuilder.fromHttpUrl(omdbApiConfig.getUrl())
                .queryParam("t", title)
                .queryParam("apikey", omdbApiConfig.getKey())
                .toUriString();

        System.out.println("Requesting OMDb URL: " + url);
        return restTemplate.getForObject(url, OmdbMovieDto.class);
    }

    public OmdbMovieDto getMovieById(String imdbId) {
        String url = UriComponentsBuilder.fromHttpUrl(omdbApiConfig.getUrl())
                .queryParam("i", imdbId)
                .queryParam("apikey", omdbApiConfig.getKey())
                .toUriString();
        return restTemplate.getForObject(url, OmdbMovieDto.class);
    }

}