package org.example.demomodule3app.controller;


import org.example.demomodule3app.dto.BookDTO;
import org.example.demomodule3app.entity.Book;
import org.example.demomodule3app.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/books")
public class BookControllerV2 {

    private final BookService bookService;

    public BookControllerV2(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping("/{id}")
    public BookDTO getBookV2(@PathVariable int id) {
        Book book = bookService.getBookById(id);
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), "Extra info");
    }
}
