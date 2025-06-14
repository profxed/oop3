package com.example.moviewatchlist.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "movies")
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: Generates a no-argument constructor
@AllArgsConstructor // Lombok: Generates a constructor with all fields
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imdbId; // To store OMDb's unique ID for external lookup
    private String title;
    private String year;
    private String director;
    private String genre;

    @Column(length = 2048) // Increased length for potentially long similar movies string
    private String similarMovies; // For simplicity, storing as a comma-separated string or JSON string
    private String imageFilePath; // Local path where downloaded images are saved
    private boolean watched = false; // Default to not watched
    private Integer rating; // Rating from 1-5
}