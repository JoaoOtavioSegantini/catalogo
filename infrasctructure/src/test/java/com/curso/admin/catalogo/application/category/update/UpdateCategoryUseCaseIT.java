package com.curso.admin.catalogo.application.category.update;

import com.curso.admin.catalogo.IntegrationTest;
import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.exceptions.DomainException;
import com.curso.admin.catalogo.domain.exceptions.NotFoundException;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryJpaEntity;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallUpdateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("film", null, true);

        save(aCategory);

        final var expectedId = aCategory.getId();
        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(1, repository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
      //  Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());

    }
    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory = Category.newCategory("film", null, true);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();

        save(aCategory);

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);



        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(0)).update(any());

    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCalledUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory = Category.newCategory("film", null, true);

        save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),
                expectedName, expectedDescription, expectedIsActive);


        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        final var actualCategory = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    //    Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
      //  Assertions.assertEquals(aCategory.getUpdatedAt(),actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldException() {
        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();

        save(aCategory);

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),expectedName, expectedDescription, expectedIsActive);
        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(any());


        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        final var actualCategory = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.isActive());
      //  Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
     //   Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
    }
    @Test
    public void givenAInvalidIDCommand_whenCallUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = "123";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(expectedId,
                expectedName, expectedDescription, expectedIsActive);


        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(CategoryID.from(expectedId)));
        Mockito.verify(categoryGateway, Mockito.times(0)).update(any());

    }

    private void save(final Category... aCategory) {
        repository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }
}
