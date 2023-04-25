package com.curso.admin.catalogo.infrastructure.category.persistence;

import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenANullName_whenCallsSave_shouldReturnAnError(){
        final var expectedMessageError = "not-null property references a null or transient value : com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryJpaEntity.name";
        final var expectedProperty = "name";
        final var aCategory = Category.newCategory("Filmes", "a categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessageError, actualCause.getMessage());

    }
    @Test
    public void givenANullCreatedAt_whenCallsSave_shouldReturnAnError(){
        final var expectedMessageError = "not-null property references a null or transient value : com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryJpaEntity.createdAt";
        final var expectedProperty = "createdAt";
        final var aCategory = Category.newCategory("Filmes", "a categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessageError, actualCause.getMessage());

    }
    @Test
    public void givenANullUpdatedAt_whenCallsSave_shouldReturnAnError(){
        final var expectedMessageError = "not-null property references a null or transient value : com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryJpaEntity.updatedAt";
        final var expectedProperty = "updatedAt";
        final var aCategory = Category.newCategory("Filmes", "a categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessageError, actualCause.getMessage());

    }
}
