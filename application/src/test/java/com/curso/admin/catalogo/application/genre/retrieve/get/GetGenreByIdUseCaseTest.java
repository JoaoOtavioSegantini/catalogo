package com.curso.admin.catalogo.application.genre.retrieve.get;

import com.curso.admin.catalogo.application.UseCaseTest;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.exceptions.DomainException;
import com.curso.admin.catalogo.domain.genre.Genre;
import com.curso.admin.catalogo.domain.genre.GenreGateway;
import com.curso.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        final var expectedName = "Ação";
        final var expectedCategories = List.<CategoryID>of(CategoryID.from("123"), CategoryID.from("456"));
        final var expectedIsActive = true;
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        final var actualGenre = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
        Assertions.assertEquals(expectedId, actualGenre.id());
    }

    private List<String> asString(List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }

    @Test
    public void givenAInvalidId_whenCallsGetGenre_shouldReturnNotFound() {

        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                DomainException.class, () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {

        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Gateway error";

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class, () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    }


}