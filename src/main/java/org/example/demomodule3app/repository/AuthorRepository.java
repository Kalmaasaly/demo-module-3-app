package org.example.demomodule3app.repository;

import org.example.demomodule3app.entity.Author;
import org.example.demomodule3app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findByNameContainingIgnoreCase(String name);


    // JOIN FETCH loads author + books in one SQL query
    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books WHERE a.id = :id")
    Optional<Author> findWithBooks(@Param("id") Integer id);

    // For ManyToMany — load book with its categories eagerly
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.categories WHERE b.id = :id")
    Optional<Book> findBookWithCategories(@Param("id") Integer id);
}
