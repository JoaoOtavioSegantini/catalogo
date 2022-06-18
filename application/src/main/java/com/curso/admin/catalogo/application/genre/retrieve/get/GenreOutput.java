package com.curso.admin.catalogo.application.genre.retrieve.get;

import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.genre.Genre;
import com.curso.admin.catalogo.domain.genre.GenreID;

import java.time.Instant;
import java.util.List;

public record GenreOutput(
        GenreID id,
        String name,
        List<String> categories,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static GenreOutput from(final Genre aGenre) {
        return new GenreOutput(
                aGenre.getId(),
                aGenre.getName(),
                aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
        );
    }
}
