package com.curso.admin.catalogo.application.genre.retrieve.list;

import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.genre.Genre;
import com.curso.admin.catalogo.domain.genre.GenreID;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
        GenreID id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt

) {
    public static GenreListOutput from(final Genre aGenre){
        return new GenreListOutput(
                aGenre.getId(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
                aGenre.getCreatedAt(),
                aGenre.getDeletedAt()
        );
    }
}
