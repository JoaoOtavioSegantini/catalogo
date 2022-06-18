package com.curso.admin.catalogo.application.genre.retrieve.list;

import com.curso.admin.catalogo.application.UseCaseTest;
import com.curso.admin.catalogo.domain.genre.Genre;
import com.curso.admin.catalogo.domain.genre.GenreGateway;
import com.curso.admin.catalogo.domain.pagination.Pagination;
import com.curso.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidQuery_thenCallsListGenres_shouldReturnGenres() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        final var query = new SearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection
        );

        final var genres = List.of(
                Genre.newGenre("Ação", true),
                Genre.newGenre("Drama", true)
        );

        final var expectedPagination = new Pagination<>(
                expectedPage, expectedPerPage, genres.size(), genres
        );

        final var expectedResult = expectedPagination.map(GenreListOutput::from);


        Mockito.when(genreGateway.findAll(Mockito.eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(genres.size(), actualResult.total());

    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyGenres() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;

        final var query = new SearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection
        );

        final var genres = List.<Genre>of();

        final var expectedPagination = new Pagination<>(
                expectedPage, expectedPerPage, genres.size(), genres
        );

        final var expectedResult = expectedPagination.map(GenreListOutput::from);


        Mockito.when(genreGateway.findAll(Mockito.eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(genres.size(), actualResult.total());

    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var query = new SearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection
        );


        Mockito.when(genreGateway.findAll(Mockito.eq(query)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());


    }
}