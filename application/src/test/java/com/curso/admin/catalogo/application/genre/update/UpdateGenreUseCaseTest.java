package com.curso.admin.catalogo.application.genre.update;

import com.curso.admin.catalogo.application.UseCaseTest;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.exceptions.NotificationException;
import com.curso.admin.catalogo.domain.genre.Genre;
import com.curso.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


public class UpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    public void givenAValidGenre_whenCallsUpdateGenre_shouldReturnGenreId(){
        final var aGenre = Genre.newGenre("titulo", false);
        final var expectedID = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(
                expectedID.getValue(),
                expectedName,
                expectedIsActive,
               asStrings(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));

        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedID.getValue(), actualOutput.id());


        Mockito.verify(genreGateway, times(1)).findById(eq(expectedID));
        Mockito.verify(genreGateway, times(1)).update(argThat(aUpdated ->
                Objects.equals(expectedName, aUpdated.getName())
                        && Objects.equals(expectedID, aUpdated.getId())
                        && Objects.equals(expectedIsActive, aUpdated.isActive())
                        && Objects.equals(expectedCategories, aUpdated.getCategories())
                        && aGenre.getUpdatedAt().isBefore(aUpdated.getUpdatedAt())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdated.getCreatedAt())
                        && Objects.isNull(aUpdated.getDeletedAt())
        ));

    }
    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateGenre_shouldReturnGenreId(){
        final var aGenre = Genre.newGenre("titulo", false);
        final var expectedID = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aCommand = UpdateGenreCommand.with(
                expectedID.getValue(),
                expectedName,
                expectedIsActive,
                asStrings(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(categoryGateway.existsByID(any())).thenReturn(expectedCategories);

        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedID.getValue(), actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).existsByID(eq(expectedCategories));

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedID));
        Mockito.verify(genreGateway, times(1)).update(argThat(aUpdated ->
                Objects.equals(expectedName, aUpdated.getName())
                        && Objects.equals(expectedID, aUpdated.getId())
                        && Objects.equals(expectedIsActive, aUpdated.isActive())
                        && Objects.equals(expectedCategories, aUpdated.getCategories())
                        && aGenre.getUpdatedAt().isBefore(aUpdated.getUpdatedAt())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdated.getCreatedAt())
                        && Objects.isNull(aUpdated.getDeletedAt())
        ));

    }

    @Test
    public void givenAInValidNullGenreName_whenCallsUpdateGenre_shouldReturnException(){
        final var aGenre = Genre.newGenre("titulo", false);
        final var expectedID = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateGenreCommand.with(
                expectedID.getValue(),
                expectedName,
                expectedIsActive,
                asStrings(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> useCase.execute(aCommand)
        ) ;

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());


        Mockito.verify(genreGateway, times(1)).findById(eq(expectedID));


    }
    @Test
    public void givenAInValidEmptyGenreName_whenCallsUpdateGenre_shouldReturnException(){
        final var aGenre = Genre.newGenre("titulo", false);
        final var expectedID = aGenre.getId();
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aCommand = UpdateGenreCommand.with(
                expectedID.getValue(),
                expectedName,
                expectedIsActive,
                asStrings(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> useCase.execute(aCommand)
        ) ;

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());


        Mockito.verify(genreGateway, times(1)).findById(eq(expectedID));


    }
    @Test
    public void givenAInValidCategoriesAndEmptyGenreName_whenCallsUpdateGenre_shouldReturnException(){
        final var aGenre = Genre.newGenre("titulo", false);
        final var expectedID = aGenre.getId();
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var movies = CategoryID.from("789");
        final var expectedCategories = List.<CategoryID>of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );
        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories could not be found: 123, 456";
        final var expectedErrorMessageTwo = "'name' should not be empty";

        final var aCommand = UpdateGenreCommand.with(
                expectedID.getValue(),
                expectedName,
                expectedIsActive,
                asStrings(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(categoryGateway.existsByID(any())).thenReturn(List.of(movies));

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> useCase.execute(aCommand)
        ) ;

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());


        Mockito.verify(genreGateway, times(1)).findById(eq(expectedID));
        Mockito.verify(categoryGateway, times(1)).existsByID(any());


    }
    private List<String> asStrings(List<CategoryID> ids) {
        return ids.stream().map(CategoryID::getValue).toList();
    }


}