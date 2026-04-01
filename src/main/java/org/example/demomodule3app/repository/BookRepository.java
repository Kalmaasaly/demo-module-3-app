package org.example.demomodule3app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.example.demomodule3app.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT b FROM Book b WHERE " +
            "(:author IS NULL OR LOWER(b.author.name) = LOWER(:author)) AND " +
            "(:title  IS NULL OR LOWER(b.title)       = LOWER(:title))")
    List<Book> search(@Param("author") String author,
                      @Param("title")  String title);
}
