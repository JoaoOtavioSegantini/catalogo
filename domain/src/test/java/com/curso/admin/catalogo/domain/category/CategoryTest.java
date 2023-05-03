package com.curso.admin.catalogo.domain.category;

import com.curso.admin.catalogo.domain.exceptions.DomainException;
import com.curso.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void  givenAInvalidParams_whenCallNewCategory_thenInstantiateACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }

    @Test
    public void  givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var exceptionErrorMessage = "'name' should not be null";
        final var exceptionErrorCount = 1;

        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var err = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(exceptionErrorMessage,err.getErrors().get(0).message());
        Assertions.assertEquals(exceptionErrorCount,err.getErrors().size());

    }

    @Test
    public void  givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = " ";
        final var exceptionErrorMessage = "'name' should not be empty";
        final var exceptionErrorCount = 1;

        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var err = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(exceptionErrorMessage,err.getErrors().get(0).message());
        Assertions.assertEquals(exceptionErrorCount,err.getErrors().size());

    }

    @Test
    public void  givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "oi ";
        final var exceptionErrorMessage = "'name' must be between 3 and 255 characters";
        final var exceptionErrorCount = 1;

        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var err = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(exceptionErrorMessage,err.getErrors().get(0).message());
        Assertions.assertEquals(exceptionErrorCount,err.getErrors().size());

    }

    @Test
    public void  givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = """
                Nunca é demais lembrar o peso e o significado destes problemas, uma vez que o fenômeno da Internet
                 possibilita uma melhor visão global dos modos de operação convencionais. A prática cotidiana prova 
                 que o surgimento do comércio virtual talvez venha a ressaltar a relatividade das condições 
                 inegavelmente apropriadas. É claro que a crescente influência da mídia prepara-nos para enfrentar 
                 situações atípicas decorrentes dos índices pretendidos. O incentivo ao avanço tecnológico,
                 assim como o comprometimento entre as equipes agrega valor ao estabelecimento das formas de ação.
                """;
        final var exceptionErrorMessage = "'name' must be between 3 and 255 characters";
        final var exceptionErrorCount = 1;

        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var err = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(exceptionErrorMessage,err.getErrors().get(0).message());
        Assertions.assertEquals(exceptionErrorCount,err.getErrors().size());

    }
    @Test
    public void  givenValidEmptyDescription_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Filmes";
        final var exceptionErrorCount = 1;

        final var expectedDescription = " ";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }
    @Test
    public void  givenAValidFalseIsActive_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Filmes";
        final var exceptionErrorCount = 1;

        final var expectedDescription = " ";
        final var expectedIsActive = false;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }
    @Test
    public void givenAVValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        final String expectedName = "Filmes";

        final var expectedDescription = "A categoria mais assistida";

        final var expectedIsActive = false;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, true);
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.deactivate();
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
      //  Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAVValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        final String expectedName = "Filmes";

        final var expectedDescription = "A categoria mais assistida";

        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, false);
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.activate();
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
     //   Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAVValidCategory_whenCallUpdate_thenReturnCategoryUpdated() throws InterruptedException {
        final String expectedName = "Filmes";
        final var exceptionErrorCount = 1;

        final var expectedDescription = "A categoria mais assistida";

        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Filme", "A categoria", false);
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();
        final var createdAt = aCategory.getCreatedAt();

        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getDeletedAt());

        Thread.sleep(200);

        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAVValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated() {
        final String expectedName = "Filmes";

        final var expectedDescription = "A categoria mais assistida";

        final var expectedIsActive = false;

        final var aCategory = Category.newCategory("Filme", "A categoria", true);
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();
        final var createdAt = aCategory.getCreatedAt();

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
       // Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
        Assertions.assertFalse(actualCategory.isActive());
    }

}
