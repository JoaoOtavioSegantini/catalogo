package com.curso.admin.catalogo.infrastructure.api.controllers;

import com.curso.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.curso.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.curso.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.curso.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.curso.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.curso.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.curso.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.curso.admin.catalogo.domain.pagination.Pagination;
import com.curso.admin.catalogo.domain.pagination.SearchQuery;
import com.curso.admin.catalogo.infrastructure.api.GenreAPI;
import com.curso.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.curso.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.curso.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.curso.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.curso.admin.catalogo.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final ListGenreUseCase listGenreUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final ListGenreUseCase listGenreUseCase,
            final UpdateGenreUseCase updateGenreUseCase
    ) {
        this.createGenreUseCase = createGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
        this.listGenreUseCase = listGenreUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(
                input.name(),
                input.isActive(),
                input.categories()
        );

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);    }

    @Override
    public Pagination<GenreListResponse> list(String search, int page, int perPage, String sort, String direction) {
        return this.listGenreUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(GenreApiPresenter::present);    }

    @Override
    public GenreResponse getById(String id) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateGenreRequest input) {
        final var aCommand = UpdateGenreCommand.with(
                id,
                input.name(),
                input.isActive(),
                input.categories()
        );

        final var output = this.updateGenreUseCase.execute(aCommand);

        return ResponseEntity.ok(output);    }

    @Override
    public void deleteById(String id) {
        this.deleteGenreUseCase.execute(id);

    }
}
