package com.curso.admin.catalogo.application.category.delete;

import com.curso.admin.catalogo.IntegrationTest;
import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryJpaEntity;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var exceptedId = aCategory.getId();

        Assertions.assertEquals(0, repository.count());

        save(aCategory);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertDoesNotThrow(() -> useCase.execute(exceptedId.getValue()));

        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOk(){
        final var exceptedId = CategoryID.from("123");

        Assertions.assertEquals(0, repository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(exceptedId.getValue()));
        Mockito.verify(categoryGateway, Mockito.times(1)).deletedById(eq(exceptedId));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var exceptedId = aCategory.getId();

        doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).deletedById(eq(exceptedId));

        Assertions.assertThrows(IllegalStateException.class,() -> useCase.execute(exceptedId.getValue()));
        Mockito.verify(categoryGateway, Mockito.times(1)).deletedById(eq(exceptedId));
    }

    private void save(final Category... aCategory) {
        repository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }
}
