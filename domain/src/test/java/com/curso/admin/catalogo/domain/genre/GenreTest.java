package com.curso.admin.catalogo.domain.genre;

import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    @Test
    public void givenAValidParams_whenCallNewGenre_shouldInstantiateAGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);


        Assertions.assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidNullName_whenCallNewGenre_shouldReceiveAError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var errorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions.assertThrows(
                NotificationException.class,() -> Genre.newGenre(expectedName, expectedIsActive)
        );


        assertEquals(errorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void  givenAnInvalidEmptyName_whenCallNewGenreAndValidate_thenShouldReceiveError() {
        final String expectedName = " ";
        final var exceptionErrorMessage = "'name' should not be empty";
        final var exceptionErrorCount = 1;
        final var expectedIsActive = true;

        final var err = Assertions.assertThrows(
                NotificationException.class,() -> Genre.newGenre(expectedName, expectedIsActive)
        );

        Assertions.assertEquals(exceptionErrorMessage,err.getErrors().get(0).message());
        Assertions.assertEquals(exceptionErrorCount,err.getErrors().size());

    }

    @Test
    public void  givenAnInvalidNameLengthLessThan3_whenCallNewGenreAndValidate_thenShouldReceiveError() {
        final String expectedName = "oi ";
        final var exceptionErrorMessage = "'name' must be between 3 and 255 characters";
        final var exceptionErrorCount = 1;
        final var expectedIsActive = true;

        final var actualException = Assertions.assertThrows(
                NotificationException.class,() -> Genre.newGenre(expectedName, expectedIsActive)
        );
        Assertions.assertEquals(exceptionErrorMessage,actualException.getErrors().get(0).message());
        Assertions.assertEquals(exceptionErrorCount,actualException.getErrors().size());

    }

    @Test
    public void  givenAnInvalidNameLengthMoreThan255_whenCallNewGenreAndValidate_thenShouldReceiveError() {
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
        final var expectedIsActive = true;

        final var err = Assertions.assertThrows(
                NotificationException.class,() -> Genre.newGenre(expectedName, expectedIsActive)
        );

        Assertions.assertEquals(exceptionErrorMessage,err.getErrors().get(0).message());
        Assertions.assertEquals(exceptionErrorCount,err.getErrors().size());

    }
    @Test
    public void givenAActivatedGenre_whenCallInactivated_shouldReceiveOk() throws InterruptedException {
        final String expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var aGenre = Genre.newGenre(expectedName, true);

        final var updatedAt = aGenre.getUpdatedAt();

        Assertions.assertNull(aGenre.getDeletedAt());
        Thread.sleep(20);
        final var actualGenre = aGenre.deactivate();
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertEquals(aGenre.getId(),actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }
    @Test
    public void givenAInactivatedGenre_whenCallActivated_shouldReceiveOk() throws InterruptedException {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var aGenre = Genre.newGenre(expectedName, false);

        Assertions.assertDoesNotThrow(() -> Genre.newGenre(expectedName, expectedIsActive));


        final var updatedAt = aGenre.getUpdatedAt();

        Assertions.assertNotNull(aGenre.getDeletedAt());
        Thread.sleep(200);

        final var actualGenre = aGenre.activate();
        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(aGenre.getId(),actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenAValidGenre_whenCallUpdated_shouldReceiveGenreUpdated() throws InterruptedException {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var aGenre = Genre.newGenre("açççç", false);

        final var updatedAt = aGenre.getUpdatedAt();

        Assertions.assertNotNull(aGenre.getDeletedAt());

        Thread.sleep(20);

        final var actualGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories);
        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(aGenre.getId(),actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenAValidGenre_whenCallUpdatedWithInactivated_shouldReceiveGenreUpdated() throws InterruptedException {
        final String expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var aGenre = Genre.newGenre("açççç", true);

        final var updatedAt = aGenre.getUpdatedAt();


        Assertions.assertNull(aGenre.getDeletedAt());
        Thread.sleep(20);

        final var actualGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories);
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(aGenre.getId(),actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenAValidGenre_whenCallUpdatedWithEmptyName_shouldReceiveError() {
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aGenre = Genre.newGenre("açççç", false);

        final var err = Assertions.assertThrows(
                NotificationException.class,() -> aGenre.update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(expectedErrorMessage,err.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorsCount,err.getErrors().size());
    }
    @Test
    public void givenAValidGenre_whenCallUpdatedWithNullName_shouldReceiveError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aGenre = Genre.newGenre("açççç", false);

        final var err = Assertions.assertThrows(
                NotificationException.class,() -> aGenre.update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(expectedErrorMessage,err.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorsCount,err.getErrors().size());
    }

    @Test
    public void givenAValidGenre_whenCallUpdatedWithNullCategoriesList_shouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var aGenre = Genre.newGenre("açççç", false);
        final var updatedAt = aGenre.getUpdatedAt();

         Assertions.assertDoesNotThrow(
                 () -> aGenre.update(expectedName, expectedIsActive, expectedCategories)
        );

        final var actualGenre = aGenre.update(expectedName, expectedIsActive, null);

        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(aGenre.getId(),actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));

    }

    @Test
    public void givenAValidGenreWithEmptyCategories_whenCallAddCategories_shouldReceiveOk() throws InterruptedException {
        final String expectedName = "Ação";
        final var movieID = CategoryID.from("235");
        final var seriesID = CategoryID.from("125");
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID,movieID);

        final var aGenre = Genre.newGenre("açççç", false);
        final var updatedAt = aGenre.getUpdatedAt();

        final var actualGenre = aGenre.update(expectedName, expectedIsActive, null);
        Thread.sleep(200);
        actualGenre.addCategory(seriesID);
        actualGenre.addCategory(movieID);

        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(aGenre.getId(),actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
    }
    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategories_shouldReceiveOk() throws InterruptedException {
        final String expectedName = "Ação";
        final var movieID = CategoryID.from("235");
        final var seriesID = CategoryID.from("125");
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movieID);

        final var aGenre = Genre.newGenre("açççç", false);
        final var updatedAt = aGenre.getUpdatedAt();

        final var actualGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories);
        Thread.sleep(200);
        actualGenre.removeCategory(seriesID);

        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(aGenre.getId(),actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallAddCategoryWithNullCategories_shouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var movieID = CategoryID.from("235");
        final var seriesID = CategoryID.from("125");
        final var expectedCategories = List.of(movieID,seriesID);
        final var aGenre = Genre.newGenre("açççç", false);
        final var updatedAt = aGenre.getUpdatedAt();


        final var actualGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories);
        actualGenre.addCategory(null);

        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(aGenre.getId(),actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertFalse(actualGenre.getUpdatedAt().isBefore(updatedAt));
    }
    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategoryWithNullCategories_shouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var movieID = CategoryID.from("235");
        final var seriesID = CategoryID.from("125");
        final var expectedCategories = List.of(movieID,seriesID);
        final var aGenre = Genre.newGenre("açççç", false);
        final var updatedAt = aGenre.getUpdatedAt();


        final var actualGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories);
        actualGenre.removeCategory(null);

        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(aGenre.getId(),actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
     //   Assertions.assertFalse(actualGenre.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenAValidGenre_whenCallAddCategories_shouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var movieID = CategoryID.from("235");
        final var seriesID = CategoryID.from("125");
        final var expectedCategories = List.of(movieID,seriesID);
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

        aGenre.addCategories(expectedCategories);

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());
        Assertions.assertNotNull(aGenre.getId());
        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedCategories, aGenre.getCategories());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertNotNull(aGenre.getCreatedAt());
    }
    @Test
    public void givenAValidGenre_whenCallAddCategoriesWithEmptyList_shouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;

        final var expectedCategories = List.<CategoryID>of();
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

        aGenre.addCategories(expectedCategories);

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());
        Assertions.assertNotNull(aGenre.getId());
        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedCategories, aGenre.getCategories());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertNotNull(aGenre.getCreatedAt());
    }
    @Test
    public void givenAValidGenre_whenCallAddCategoriesWithNullList_shouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;

        final var expectedCategories = List.<CategoryID>of();
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

        aGenre.addCategories(null);

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());
        Assertions.assertNotNull(aGenre.getId());
        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedCategories, aGenre.getCategories());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertNotNull(aGenre.getCreatedAt());
    }
}