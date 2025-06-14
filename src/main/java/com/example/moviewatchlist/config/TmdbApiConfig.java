package com.example.moviewatchlist.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@ConfigurationProperties(prefix = "tmdb")
@Getter
@Setter
public class TmdbApiConfig {
    @JsonProperty("base-url")
    private String baseUrl;
    private String key;

    @JsonProperty("image-base-url")
    private String imageBaseUrl; // Maps to tmdb.image-base-url
}