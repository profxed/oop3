package com.example.moviewatchlist.repository;

import com.example.moviewatchlist.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marks this interface as a Spring repository component
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Spring Data JPA automatically provides CRUD methods (save, findById, findAll, delete, etc.)
    // You can add custom query methods here if needed, e.g., findByTitle(String title);
}