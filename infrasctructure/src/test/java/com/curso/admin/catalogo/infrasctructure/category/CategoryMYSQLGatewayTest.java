package com.curso.admin.catalogo.infrasctructure.category;

import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.category.CategorySearchQuery;
import com.curso.admin.catalogo.MySQLGatewayTest;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryJpaEntity;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@MySQLGatewayTest
public class CategoryMYSQLGatewayTest {

    @Autowired
    private CategoryMYSQLGateway categoryMYSQLGateway;

    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnNewCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, repository.count());

        final var actualCategory = categoryMYSQLGateway.create(aCategory);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertNull(actualCategory.getDeletedAt());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());

        final var aEntity = repository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(expectedName, aEntity.getName());
        Assertions.assertEquals(expectedDescription, aEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, aEntity.isActive());
        Assertions.assertEquals(aCategory.getId().getValue(), aEntity.getId());
        Assertions.assertNull(aEntity.getDeletedAt());
        Assertions.assertEquals(aCategory.getCreatedAt(), aEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), aEntity.getUpdatedAt());

    }

    @Test
    public void givenAValidCategory_whenCallsUpdated_shouldReturnCategoryUpdated(){
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("film", null, expectedIsActive);

        Assertions.assertEquals(0, repository.count());
        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, repository.count());

        final var aUpdatedCattegory = aCategory
                .clone()
                .update(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryMYSQLGateway.update(aUpdatedCattegory);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertNull(actualCategory.getDeletedAt());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));

        final var aEntity = repository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(expectedName, aEntity.getName());
        Assertions.assertEquals(expectedDescription, aEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, aEntity.isActive());
        Assertions.assertEquals(aCategory.getId().getValue(), aEntity.getId());
        Assertions.assertNull(aEntity.getDeletedAt());
        Assertions.assertEquals(aCategory.getCreatedAt(), aEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));

    }

    @Test
    public void givenAPPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory(){
        final var aCategory = Category.newCategory("film", null, true);
        Assertions.assertEquals(0, repository.count());
        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, repository.count());

        categoryMYSQLGateway.deletedById(aCategory.getId());
        Assertions.assertEquals(0, repository.count());

    }
    @Test
    public void givenAInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory(){

        Assertions.assertEquals(0, repository.count());

        categoryMYSQLGateway.deletedById(CategoryID.from("Invalid"));
        Assertions.assertEquals(0, repository.count());

    }

    @Test
    public void givenAPPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, repository.count());
        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, repository.count());

        final var actualCategory = categoryMYSQLGateway.findById(aCategory.getId()).get();

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertNull(actualCategory.getDeletedAt());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),actualCategory.getUpdatedAt());


    }
    @Test
    public void givenACategoryIdNotStored_whenCallsFindById_shouldReturnEmpty(){
        Assertions.assertEquals(0, repository.count());

        final var actualCategory = categoryMYSQLGateway.findById(CategoryID.from("empty"));

        Assertions.assertTrue(actualCategory.isEmpty());

    }
    @Test
    public void givenAPrePersistedCategories_whenCallsFindAll_shouldReturnCategories(){
          final var expectedPerPage = 1;
          final var expectedTotal = 3;
          final var expectedPage = 0;

          final var series = Category.newCategory("Series", null, true);
          final var documentarios = Category.newCategory("Documentários", null, true);
          final var filmes = Category.newCategory("Filmes", null, true);

          Assertions.assertEquals(0, repository.count());

          repository.saveAll(List.of(
                  CategoryJpaEntity.from(series),
                  CategoryJpaEntity.from(filmes),
                  CategoryJpaEntity.from(documentarios)
          ));

        Assertions.assertEquals(3, repository.count());
        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var result = categoryMYSQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(documentarios.getId(), result.items().get(0).getId());


    }
    @Test
    public void givenAEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage(){
        final var expectedPerPage = 1;
        final var expectedTotal = 0;
        final var expectedPage = 0;

        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);
        final var filmes = Category.newCategory("Filmes", null, true);

        Assertions.assertEquals(0, repository.count());


        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var result = categoryMYSQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(0, result.items().size());


    }
    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated(){
        final var expectedPerPage = 1;
        final var expectedTotal = 3;
        var expectedPage = 0;

        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);
        final var filmes = Category.newCategory("Filmes", null, true);

        Assertions.assertEquals(0, repository.count());

        repository.saveAll(List.of(
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, repository.count());
         var query = new CategorySearchQuery(0, 1, "", "name", "asc");
         var result = categoryMYSQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(documentarios.getId(), result.items().get(0).getId());

        // Page 1
        expectedPage = 1;
        query = new CategorySearchQuery(1, 1, "", "name", "asc");
        result = categoryMYSQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(filmes.getId(), result.items().get(0).getId());

        // Page 2
        expectedPage = 2;
        query = new CategorySearchQuery(2, 1, "", "name", "asc");
        result = categoryMYSQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(series.getId(), result.items().get(0).getId());
    }

    @Test
    public void givenAPrePersistedCategoriesAndDocAsTerms_whenCallsFindAll_shouldReturnPaginated(){
        final var expectedPerPage = 1;
        final var expectedTotal = 1;
        final var expectedPage = 0;

        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);
        final var filmes = Category.newCategory("Filmes", null, true);

        Assertions.assertEquals(0, repository.count());

        repository.saveAll(List.of(
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, repository.count());
       final var query = new CategorySearchQuery(0, 1, "doc", "name", "asc");
       final var result = categoryMYSQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(documentarios.getId(), result.items().get(0).getId());


    }

    @Test
    public void givenAPrePersistedCategoriesAndDescriptionTerms_whenCallsFindAll_shouldReturnPaginated(){
        final var expectedPerPage = 1;
        final var expectedTotal = 1;
        final var expectedPage = 0;

        final var series = Category.newCategory("Series", "A categoria mais assistida", true);
        final var documentarios = Category.newCategory("Documentários", "Uma categoria assistida", true);
        final var filmes = Category.newCategory("Filmes", "A categoria menos assistida", true);

        Assertions.assertEquals(0, repository.count());

        repository.saveAll(List.of(
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, repository.count());
        final var query = new CategorySearchQuery(0, 1, "mais assistida", "description", "asc");
        final var result = categoryMYSQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(series.getId(), result.items().get(0).getId());


    }
}
