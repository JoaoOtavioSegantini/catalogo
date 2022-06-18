package com.curso.admin.catalogo.application.genre.create;

import com.curso.admin.catalogo.domain.genre.Genre;

public record CreateGenreOutput(String id) {

    public static CreateGenreOutput from(final String anID) {
        return new CreateGenreOutput(anID);
    }

    public static CreateGenreOutput from(final Genre aGenre) {
        return new CreateGenreOutput(aGenre.getId().getValue());
    }
}
