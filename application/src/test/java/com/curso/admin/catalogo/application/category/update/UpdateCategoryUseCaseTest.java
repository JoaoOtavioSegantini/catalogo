package com.curso.admin.catalogo.application.category.update;

import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.exceptions.DomainException;
import com.curso.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallUpdateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("film", null, true);

        final var expectedId = aCategory.getId();
        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(
                aUpdatedCategory ->
                Objects.equals(expectedName, aUpdatedCategory.getName())
                        && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                        && Objects.equals(expectedId, aUpdatedCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())

        ));

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

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(0)).update(Mockito.any());

    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCalledUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),
                expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(),aUpdatedCategory.getCreatedAt())
                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.nonNull(aUpdatedCategory.getDeletedAt())
                ));
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

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),expectedName, expectedDescription, expectedIsActive);
        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));
        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .update(Mockito.argThat(updatedCategory ->
                        Objects.equals(expectedName, updatedCategory.getName())
                                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, updatedCategory.isActive())
                                && Objects.nonNull(updatedCategory.getId())
                                && Objects.nonNull(updatedCategory.getCreatedAt())
                                && Objects.nonNull(updatedCategory.getUpdatedAt())
                                && Objects.isNull(updatedCategory.getDeletedAt())
                ));
    }
    @Test
    public void givenAInvalidIDCommand_whenCallUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(expectedId,
                expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(CategoryID.from(expectedId)));
        Mockito.verify(categoryGateway, Mockito.times(0)).update(Mockito.any());

    }
}
