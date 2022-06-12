package com.curso.admin.catalogo.application.category.create;

import com.curso.admin.catalogo.IntegrationTest;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCalledCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        Assertions.assertEquals(0, repository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Assertions.assertEquals(1, repository.count());

        final var actualCategory = repository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }
    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        Assertions.assertEquals(0, repository.count());

        verify(categoryGateway, times(0)).create(any());


    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCalledCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = false;

        Assertions.assertEquals(0, repository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);


        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Assertions.assertEquals(1, repository.count());

        final var actualCategory = repository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).create(any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());


    }

}
