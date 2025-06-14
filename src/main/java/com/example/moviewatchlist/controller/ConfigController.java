package com.example.moviewatchlist.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


// ENV testing controller to expose configuration properties
// for now...
@RestController
public class ConfigController {

    @Value("${omdb.key}")
    private String omdbKey;
    
    @Value("${tmdb.key}")
    private String tmdbKey;
    
    @Value("${omdb.url}")
    private String omdbUrl;
    
    @Value("${tmdb.base-url}")
    private String tmdbBaseUrl;

    @GetMapping("/config")
    public Map<String, String> showConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("omdbKey", omdbKey);
        config.put("tmdbKey", tmdbKey);
        config.put("omdbUrl", omdbUrl);
        config.put("tmdbBaseUrl", tmdbBaseUrl);
        return config;
    }
}