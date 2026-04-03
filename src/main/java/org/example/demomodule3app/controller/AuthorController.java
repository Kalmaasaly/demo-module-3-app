package org.example.demomodule3app.controller;


import jakarta.validation.Valid;
import org.example.demomodule3app.entity.Author;
import org.example.demomodule3app.entity.Book;
import org.example.demomodule3app.service.AuthorService;
import org.example.demomodule3app.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@CrossOrigin("*")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();  // add this method to AuthorService too
    }

    @GetMapping("/{id}")
    public Author getAuthorWithBooks(@PathVariable Integer id) {
        return authorService.getAuthorWithBooks(id);
    }
    @PostMapping
    public ResponseEntity<Author> createBook(@Valid @RequestBody Author author) {
        Author createdAuthor = authorService.createAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }
    // PUT /books/{id}
    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable int id, @Valid @RequestBody Author author) {
        return authorService.updateAuthor(id, author);
    }

}
