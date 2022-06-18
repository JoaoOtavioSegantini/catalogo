package com.curso.admin.catalogo.application.genre.retrieve.list;

import com.curso.admin.catalogo.application.UseCase;
import com.curso.admin.catalogo.domain.pagination.Pagination;
import com.curso.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
