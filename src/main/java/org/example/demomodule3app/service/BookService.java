package org.example.demomodule3app.service;

import org.example.demomodule3app.entity.Book;
import org.example.demomodule3app.exception.BookNotFoundException;
import org.example.demomodule3app.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    public Book getBookById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    public Book createBook(Book book) {
        return repository.save(book);
    }

    public Book updateBook(int id, Book book) {
        if (!repository.existsById(id))
            throw new BookNotFoundException("Book not found with id: " + id);
        book.setId(id);
        return repository.save(book);
    }

    public void deleteBook(int id) {
        if (!repository.existsById(id))
            throw new BookNotFoundException("Book not found with id: " + id);
        repository.deleteById(id);
    }
    public Book patchBook(int id, Book book) {
        Book existingBook = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        if (book.getTitle() != null) existingBook.setTitle(book.getTitle());
        if (book.getAuthor() != null) existingBook.setAuthor(book.getAuthor());

        return repository.save(existingBook);
    }
    public List<Book> searchBooks(String author, String title) {
        List<Book> allBooks = repository.findAll();

        return allBooks.stream()
                .filter(book -> (author == null || book.getAuthor().equalsIgnoreCase(author)))
                .filter(book -> (title == null || book.getTitle().equalsIgnoreCase(title)))
                .toList();
    }
}