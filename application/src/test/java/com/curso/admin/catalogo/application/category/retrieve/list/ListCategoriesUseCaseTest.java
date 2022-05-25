package com.curso.admin.catalogo.application.category.retrieve.list;

import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategorySearchQuery;
import com.curso.admin.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidQuery_thenCallsListCategories_shouldReturnCategories() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        final var query = new CategorySearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection
        );

        final var categories = List.of(
               Category.newCategory("Filmes", null, true),
               Category.newCategory("Séries", null, true)
        );

        final var expectedPagination = new Pagination<>(
                expectedPage, expectedPerPage, categories.size(), categories
        );

        final var expectedResult = expectedPagination.map(CategoryListOutput::from);


        Mockito.when(categoryGateway.findAll(Mockito.eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());

    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;

        final var query = new CategorySearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection
        );

        final var categories = List.<Category>of();

        final var expectedPagination = new Pagination<>(
                expectedPage, expectedPerPage, categories.size(), categories
        );

        final var expectedResult = expectedPagination.map(CategoryListOutput::from);


        Mockito.when(categoryGateway.findAll(Mockito.eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());

    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;
        final var expectedErrorMessage = "Gateway error";

        final var query = new CategorySearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection
        );


        Mockito.when(categoryGateway.findAll(Mockito.eq(query)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());


    }
}
