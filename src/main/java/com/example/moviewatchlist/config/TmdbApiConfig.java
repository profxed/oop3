package main.java.com.example.moviewatchlist.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tmdb")
@Getter
@Setter
public class TmdbApiConfig {
    private String baseUrl;
    private String key;
    private String imageBaseUrl;
}