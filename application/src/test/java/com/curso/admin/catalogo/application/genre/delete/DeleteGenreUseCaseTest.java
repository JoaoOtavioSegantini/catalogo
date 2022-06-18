package com.curso.admin.catalogo.application.genre.delete;

import com.curso.admin.catalogo.application.UseCaseTest;
import com.curso.admin.catalogo.domain.genre.Genre;
import com.curso.admin.catalogo.domain.genre.GenreGateway;
import com.curso.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteGenre_shouldBeOk(){
        final var aGenre = Genre.newGenre("Acao", true);
        final var exceptedId = aGenre.getId();

        Mockito.doNothing().when(genreGateway).deleteById(Mockito.eq(exceptedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(exceptedId.getValue()));
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(Mockito.eq(exceptedId));
    }
    @Test
    public void givenAInvalidId_whenCallsDeleteGenre_shouldBeOk(){
        final var exceptedId = GenreID.from("123");

        Mockito.doNothing().when(genreGateway).deleteById(Mockito.eq(exceptedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(exceptedId.getValue()));
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(Mockito.eq(exceptedId));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var aGenre = Genre.newGenre("Acao", true);
        final var exceptedId = aGenre.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error")).when(genreGateway).deleteById(Mockito.eq(exceptedId));

        Assertions.assertThrows(IllegalStateException.class,() -> useCase.execute(exceptedId.getValue()));
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(Mockito.eq(exceptedId));
    }
}