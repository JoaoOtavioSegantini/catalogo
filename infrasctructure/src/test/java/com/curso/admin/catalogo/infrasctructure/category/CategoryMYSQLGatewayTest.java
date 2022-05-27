package com.curso.admin.catalogo.infrasctructure.category;

import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.infrasctructure.MySQLGatewayTest;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryJpaEntity;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


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

}
