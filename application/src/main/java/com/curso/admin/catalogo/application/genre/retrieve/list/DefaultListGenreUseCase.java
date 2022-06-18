package com.curso.admin.catalogo.application.genre.retrieve.list;

import com.curso.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.curso.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.genre.GenreGateway;
import com.curso.admin.catalogo.domain.pagination.Pagination;
import com.curso.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListGenreUseCase extends ListGenreUseCase {
    private final GenreGateway genreGateway;

    public DefaultListGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery)
                .map(GenreListOutput::from);
    }
}
