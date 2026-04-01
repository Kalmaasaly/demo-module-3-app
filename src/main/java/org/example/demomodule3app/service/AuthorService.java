package org.example.demomodule3app.service;


import org.example.demomodule3app.entity.Author;
import org.example.demomodule3app.entity.Book;
import org.example.demomodule3app.entity.Category;
import org.example.demomodule3app.exception.AuthorNotFoundException;
import org.example.demomodule3app.repository.AuthorRepository;
import org.example.demomodule3app.repository.BookRepository;
import org.example.demomodule3app.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public AuthorService(AuthorRepository authorRepository,
                         BookRepository bookRepository,
                         CategoryRepository categoryRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    // Create an author with books in one transaction
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    // Add a book to an existing author — @ManyToOne in action
    public Book addBookToAuthor(Integer authorId, Book book) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found: " + authorId));
        book.setAuthor(author);        // set the owning side
        return bookRepository.save(book);
    }

    // Assign a category to a book — @ManyToMany in action
    public Book addCategoryToBook(Integer bookId, Integer categoryId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));
        book.addCategory(category);    // uses our helper — syncs both sides
        return bookRepository.save(book);
    }

    // Load author with all their books — JOIN FETCH avoids N+1
    @Transactional(readOnly = true)
    public Author getAuthorWithBooks(Integer id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found: " + id));
    }
    public Author updateAuthor(int id, Author author) {
        if (!authorRepository.existsById(id))
            throw new AuthorNotFoundException("Author not found with id: " + id);
        author.setId(id);
        return authorRepository.save(author);
    }
}
