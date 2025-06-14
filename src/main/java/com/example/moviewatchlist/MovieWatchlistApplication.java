package com.example.moviewatchlist;

import com.example.moviewatchlist.client.OmdbApiClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class MovieWatchlistApplication {
    private static final Logger log = LoggerFactory.getLogger(OmdbApiClient.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MovieWatchlistApplication.class, args);
		checkEnvVarsLoaded(context);
	}

	private static void checkEnvVarsLoaded(ConfigurableApplicationContext context) {
		Environment env = context.getEnvironment();

		if (env.getProperty("ENV").equals("DEVELOPMENT")) {
			System.out.println("Running in DEVELOPMENT mode");

			System.out.println("OMDB Key: " + env.getProperty("OMDB_API_KEY"));
			System.out.println("TMDB Key: " + env.getProperty("TMDB_API_KEY"));
		} else if (env.getProperty("ENV").equals("PRODUCTION")) {
			System.out.println("Running in PRODUCTION mode");

			log.info("OMDB Env - URL: {}", env.getProperty("OMDB_API_KEY"));
			log.info("OMDB Env - Key: {}", env.getProperty("TMDB_API_KEY"));
		} else {
			System.out.println("Running in UNKNOWN mode");
			System.out.println("PLEASE set the ENV variable to either DEVELOPMENT or PRODUCTION");

			System.exit(1);
		}
	}
}