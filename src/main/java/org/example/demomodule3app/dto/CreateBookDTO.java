package org.example.demomodule3app.dto;

import java.util.Set;

public record CreateBookDTO(
        String title,
        Integer authorId,
        String authorName,
        Set<String> categoryNames
) {}
