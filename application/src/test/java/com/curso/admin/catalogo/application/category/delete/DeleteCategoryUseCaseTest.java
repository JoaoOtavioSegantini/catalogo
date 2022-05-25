package com.curso.admin.catalogo.application.category.delete;

import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk(){
       final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
       final var exceptedId = aCategory.getId();

       Mockito.doNothing().when(categoryGateway).deletedById(Mockito.eq(exceptedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(exceptedId.getValue()));
        Mockito.verify(categoryGateway, Mockito.times(1)).deletedById(Mockito.eq(exceptedId));
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOk(){
        final var exceptedId = CategoryID.from("123");

        Mockito.doNothing().when(categoryGateway).deletedById(Mockito.eq(exceptedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(exceptedId.getValue()));
        Mockito.verify(categoryGateway, Mockito.times(1)).deletedById(Mockito.eq(exceptedId));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var exceptedId = aCategory.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).deletedById(Mockito.eq(exceptedId));

        Assertions.assertThrows(IllegalStateException.class,() -> useCase.execute(exceptedId.getValue()));
        Mockito.verify(categoryGateway, Mockito.times(1)).deletedById(Mockito.eq(exceptedId));
    }


}
