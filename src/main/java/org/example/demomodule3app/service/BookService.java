package org.example.demomodule3app.service;

import org.example.demomodule3app.dto.CreateBookDTO;
import org.example.demomodule3app.entity.Author;
import org.example.demomodule3app.entity.Book;
import org.example.demomodule3app.entity.Category;
import org.example.demomodule3app.exception.BookNotFoundException;
import org.example.demomodule3app.repository.AuthorRepository;
import org.example.demomodule3app.repository.BookRepository;
import org.example.demomodule3app.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BookService(BookRepository repository, AuthorRepository authorRepository, CategoryRepository categoryRepository) {
        this.bookRepository = repository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    /*public Book createBook(Book book) {
        return repository.save(book);
    }*/
    public Book createBook(CreateBookDTO dto){

        Author author;

        if (dto.authorId() != null) {
            author = authorRepository.findById(dto.authorId())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
        } else if (dto.authorName() != null) {
            author = new Author();
            author.setName(dto.authorName());
            author = authorRepository.save(author);
        } else {
            throw new RuntimeException("Author is required");
        }


        Book book = new Book();
        book.setTitle(dto.title());
        book.setAuthor(author);


        if (dto.categoryNames() != null) {
            for (String name : dto.categoryNames()) {

                Category category = categoryRepository.findByName(name)
                        .orElseGet(() -> categoryRepository.save(new Category(name)));

                book.addCategory(category); // keeps both sides in sync
            }
        }


        return bookRepository.save(book);
    }

    public Book updateBook(int id, Book book) {
        if (!bookRepository.existsById(id))
            throw new BookNotFoundException("Book not found with id: " + id);
        book.setId(id);
        return bookRepository.save(book);
    }

    public void deleteBook(int id) {
        if (!bookRepository.existsById(id))
            throw new BookNotFoundException("Book not found with id: " + id);
        bookRepository.deleteById(id);
    }
    public Book patchBook(int id, Book book) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        if (book.getTitle() != null) existingBook.setTitle(book.getTitle());
        if (book.getAuthor() != null) existingBook.setAuthor(book.getAuthor());

        return bookRepository.save(existingBook);
    }
    public List<Book> searchBooks(String author, String title) {
       /* List<Book> allBooks = repository.findAll();

        return allBooks.stream()
                .filter(book -> (author == null || book.getAuthor().equalsIgnoreCase(author)))
                .filter(book -> (title == null || book.getTitle().equalsIgnoreCase(title)))
                .toList();*/

        return bookRepository.search(author,title);
    }
}