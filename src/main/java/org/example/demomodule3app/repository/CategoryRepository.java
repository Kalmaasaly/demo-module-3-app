package org.example.demomodule3app.repository;

import org.example.demomodule3app.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name); // useful for book-category linking

}
