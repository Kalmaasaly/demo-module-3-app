package org.example.demomodule3app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.example.demomodule3app.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
