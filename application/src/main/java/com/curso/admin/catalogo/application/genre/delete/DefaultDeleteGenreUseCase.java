package com.curso.admin.catalogo.application.genre.delete;

import com.curso.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.genre.GenreGateway;
import com.curso.admin.catalogo.domain.genre.GenreID;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteCategoryUseCase {

    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final String aIn) {
        this.genreGateway.deleteById(GenreID.from(aIn));
    }
}
