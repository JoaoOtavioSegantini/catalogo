package com.curso.admin.catalogo.application.genre.create;

import com.curso.admin.catalogo.application.UseCaseTest;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.exceptions.NotificationException;
import com.curso.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class CreateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }
    @Test
    public void givenAValidGenre_whenCallsCreateGenre_shouldReturnOk(){
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();


        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, times(1)).create(Mockito.argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
        ));
    }
    @Test
    public void givenAValidGenreInactivated_whenCallsCreateGenre_shouldReturnOk(){
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();


        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, times(1)).create(Mockito.argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.nonNull(aGenre.getDeletedAt())
        ));
    }
    @Test
    public void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId(){
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );


        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(categoryGateway.existsByID(any())).thenReturn(expectedCategories);
        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).existsByID(expectedCategories);

        Mockito.verify(genreGateway, times(1)).create(Mockito.argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidName_whenCallsCreateGenre_shouldReturnException(){
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.<CategoryID>of();


        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualEx = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualEx);
        Assertions.assertEquals(expectedErrorCount, actualEx.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualEx.getErrors().get(0).message());
        Mockito.verify(categoryGateway, times(0)).existsByID(any());
        Mockito.verify(genreGateway, times(0)).create(any());

    }
    @Test
    public void givenAValidNullName_whenCallsCreateGenre_shouldReturnException(){
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.<CategoryID>of();


        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualEx = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualEx);
        Assertions.assertEquals(expectedErrorCount, actualEx.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualEx.getErrors().get(0).message());
        Mockito.verify(categoryGateway, times(0)).existsByID(any());
        Mockito.verify(genreGateway, times(0)).create(any());

    }
    @Test
    public void givenAValidGenre_whenCallsCreateGenreWithSomeCategoriesDoesNotExists_shouldReturnException(){
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var movies = CategoryID.from("222");
        final var series = CategoryID.from("333");
        final var documentarios = CategoryID.from("123");
        final var expectedErrorMessage = "Some categories could not be found: 222, 123";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.of(
                movies,
                series,
                documentarios
        );
        when(categoryGateway.existsByID(any())).thenReturn(List.of(series));

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualEx = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualEx);
        Assertions.assertEquals(expectedErrorCount, actualEx.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualEx.getErrors().get(0).message());
        Mockito.verify(categoryGateway, times(1)).existsByID(any());
        Mockito.verify(genreGateway, times(0)).create(any());

    }

    @Test
    public void givenAInValidGenre_whenCallsCreateGenreWithSomeCategoriesDoesNotExists_shouldReturnTwoErrors(){
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var movies = CategoryID.from("222");
        final var series = CategoryID.from("333");
        final var documentarios = CategoryID.from("123");
        final var expectedErrorMessage1 = "Some categories could not be found: 222, 123";
        final var expectedErrorMessage2 = "'name' should not be empty";

        final var expectedErrorCount = 2;
        final var expectedCategories = List.of(
                movies,
                series,
                documentarios
        );
        when(categoryGateway.existsByID(any())).thenReturn(List.of(series));

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualEx = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualEx);
        Assertions.assertEquals(expectedErrorCount, actualEx.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage1, actualEx.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessage2, actualEx.getErrors().get(1).message());
        Mockito.verify(categoryGateway, times(1)).existsByID(any());
        Mockito.verify(genreGateway, times(0)).create(any());

    }
    private List<String> asString(final List<CategoryID> categories){
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}