package com.curso.admin.catalogo.application.genre.retrieve.get;

import com.curso.admin.catalogo.domain.exceptions.NotFoundException;
import com.curso.admin.catalogo.domain.genre.Genre;
import com.curso.admin.catalogo.domain.genre.GenreGateway;
import com.curso.admin.catalogo.domain.genre.GenreID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(final String aIn) {
        final var aGenreID = GenreID.from(aIn);

        return this.genreGateway.findById(aGenreID)
                .map(GenreOutput::from)
                .orElseThrow(notFound(aGenreID));
    }

    private Supplier<NotFoundException> notFound(final GenreID anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }

}
