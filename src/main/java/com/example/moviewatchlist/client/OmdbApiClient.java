package main.java.com.example.moviewatchlist.client;

import main.java.com.example.moviewatchlist.config.OmdbApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class OmdbApiClient {

    private final RestTemplate restTemplate;
    private final OmdbApiConfig omdbApiConfig;

    @Autowired
    public OmdbApiClient(RestTemplateBuilder restTemplateBuilder, OmdbApiConfig omdbApiConfig) {
        this.restTemplate = restTemplateBuilder.build();
        this.omdbApiConfig = omdbApiConfig;
    }

    public Map<String, Object> getMovieData(String title) {
        String url = omdbApiConfig.getUrl() + "?t=" + title + "&apikey=" + omdbApiConfig.getKey();
        // OMDB returns JSON, Spring will automatically map it to a Map<String, Object>
        return restTemplate.getForObject(url, Map.class);
    }
}