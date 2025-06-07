package main.java.com.example.moviewatchlist.client;

import main.java.com.example.moviewatchlist.config.TmdbApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class TmdbApiClient {

    private final RestTemplate restTemplate;
    private final TmdbApiConfig tmdbApiConfig;

    @Autowired
    public TmdbApiClient(RestTemplateBuilder restTemplateBuilder, TmdbApiConfig tmdbApiConfig) {
        this.restTemplate = restTemplateBuilder.build();
        this.tmdbApiConfig = tmdbApiConfig;
    }

    public Map<String, Object> searchMovie(String title) {
        String url = UriComponentsBuilder.fromUriString(tmdbApiConfig.getBaseUrl() + "search/movie")
                .queryParam("api_key", tmdbApiConfig.getKey())
                .queryParam("query", title)
                .build().toUriString();
        return restTemplate.getForObject(url, Map.class);
    }

    public Map<String, Object> getMovieDetails(String movieId) {
        String url = UriComponentsBuilder.fromUriString(tmdbApiConfig.getBaseUrl() + "movie/" + movieId)
                .queryParam("api_key", tmdbApiConfig.getKey())
                .queryParam("append_to_response", "images,similar")
                .build().toUriString();
        return restTemplate.getForObject(url, Map.class);
    }
}