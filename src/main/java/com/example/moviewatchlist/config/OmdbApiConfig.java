package com.example.moviewatchlist.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "omdb")
@Getter
@Setter
public class OmdbApiConfig {
    private String url;
    private String key;
}