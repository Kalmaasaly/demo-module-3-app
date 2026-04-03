package org.example.demomodule3app.controller;

import org.example.demomodule3app.entity.Author;
import org.example.demomodule3app.entity.Book;
import org.example.demomodule3app.entity.Category;
import org.example.demomodule3app.service.AuthorService;
import org.example.demomodule3app.service.BookService;
import org.example.demomodule3app.service.CategoryService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin("*")
public class ChatController {

    private final ChatClient chatClient;
    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public ChatController(ChatClient.Builder builder,
                          BookService bookService,
                          AuthorService authorService,
                          CategoryService categoryService) {
        this.chatClient = builder.build();
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    // ── Request & Response records ────────────────────────────────────────────
    public record ChatRequest(String question) {}
    public record ChatResponse(String answer) {}

    // ── POST /api/v1/chat ─────────────────────────────────────────────────────
    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        // 1. Load all data from the database
        List<Book> books           = bookService.getAllBooks();
        List<Author> authors       = authorService.getAllAuthors();
        List<Category> categories  = categoryService.getAllCategories();

        // 2. Format DB data as readable text for the AI
        String booksText = books.stream()
                .map(b -> String.format("- ID:%d | Title: \"%s\" | Author: %s | Categories: %s",
                        b.getId(),
                        b.getTitle(),
                        b.getAuthor() != null ? b.getAuthor().getName() : "Unknown",
                        b.getCategories().stream()
                                .map(Category::getName)
                                .collect(Collectors.joining(", "))))
                .collect(Collectors.joining("\n"));

        String authorsText = authors.stream()
                .map(a -> String.format("- ID:%d | Name: %s | Bio: %s | Books: %s",
                        a.getId(),
                        a.getName(),
                        a.getBio() != null ? a.getBio() : "No bio",
                        a.getBooks().stream()
                                .map(Book::getTitle)
                                .collect(Collectors.joining(", "))))
                .collect(Collectors.joining("\n"));

        String categoriesText = categories.stream()
                .map(c -> String.format("- ID:%d | Name: %s | Books: %s",
                        c.getId(),
                        c.getName(),
                        c.getBooks().stream()
                                .map(Book::getTitle)
                                .collect(Collectors.joining(", "))))
                .collect(Collectors.joining("\n"));

        // 3. Build the prompt with DB context + user question
        String prompt = String.format("""
                You are BookBot, an AI assistant for a book management system.
                Answer questions ONLY based on the database data below.
                Do NOT invent or guess any data — only use what is provided.
                Be clear, concise, and helpful.
                
                === BOOKS (%d total) ===
                %s
                
                === AUTHORS (%d total) ===
                %s
                
                === CATEGORIES (%d total) ===
                %s
                
                === USER QUESTION ===
                %s
                """,
                books.size(),      booksText,
                authors.size(),    authorsText,
                categories.size(), categoriesText,
                request.question()
        );

        // 4. Send to Ollama llama3 via Spring AI and return the answer
        String answer = chatClient
                .prompt(prompt)
                .call()
                .content();

        return new ChatResponse(answer);
    }
}
