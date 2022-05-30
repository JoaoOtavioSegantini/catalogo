package com.curso.admin.catalogo.application.category.retrieve.list;

import com.curso.admin.catalogo.IntegrationTest;
import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategorySearchQuery;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryJpaEntity;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

@IntegrationTest
public class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @BeforeEach
    void mockUp() {
     final var categories = List.of(
             Category.newCategory("Netflix Originals", "De autoria da Netflix", true),
                     Category.newCategory("Filmes", null, true),
                     Category.newCategory("Documentarios", null, true),
                     Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon Prime", true),
                     Category.newCategory("Sports", null, true),
                     Category.newCategory("Series", null, true),
                     Category.newCategory("Kids", "categoria para crianças", true))
             .stream()
             .map(CategoryJpaEntity::from)
             .toList();


     repository.saveAllAndFlush(categories);

    }

    @Test
    public void givenAVValidTerm_whenTermDoesNotMatchesPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "joojj okokok";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "KI,0,10,1,1,Kids",
            "crianças,0,10,1,1,Kids",
            "da Amazon,0,10,1,1,Amazon Originals"
    })
    public void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
            final String expectedTerm,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName

    ) {

        final var expectedSort = "name";
        final var expectedDirection = "asc";


        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerm, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals",
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Netflix Originals",
            "createdAt,desc,0,10,7,7,Kids"
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_shouldReturnCategoriesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedTerm = "";
        final var aQuery =
                new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerm,expectedSort,expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedTotal, actualResult.total());

    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon Originals;Documentarios",
            "1,2,2,7,Filmes;Kids",
            "2,2,2,7,Netflix Originals;Series",
            "3,2,1,7,Sports",
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesName
    ){
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerm = "";
        final var aQuery =
                new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerm,expectedSort,expectedDirection);
        int index = 0;
        final var actualResult = useCase.execute(aQuery);
        for (final String expectedName : expectedCategoriesName.split(";")) {
            final String actualName = actualResult.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }
}
