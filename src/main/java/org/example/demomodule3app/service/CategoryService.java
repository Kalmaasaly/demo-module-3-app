package org.example.demomodule3app.service;

import org.example.demomodule3app.entity.Book;
import org.example.demomodule3app.entity.Category;
import org.example.demomodule3app.repository.BookRepository;
import org.example.demomodule3app.repository.CategoryRepository;

public class CategoryService {

    public CategoryService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public Book addCategoryToBook(Integer bookId, Integer categoryId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));
        book.addCategory(category);
        return bookRepository.save(book);
    }
}
