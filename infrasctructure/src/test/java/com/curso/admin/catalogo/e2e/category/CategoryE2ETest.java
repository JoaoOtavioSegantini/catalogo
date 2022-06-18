package com.curso.admin.catalogo.e2e.category;


import com.curso.admin.catalogo.E2ETest;
import com.curso.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.infrasctructure.category.models.CreateCategoryInput;
import com.curso.admin.catalogo.infrasctructure.category.models.UpdateCategoryInput;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryRepository;
import com.curso.admin.catalogo.infrasctructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToCreateANewCategory() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualID = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = retrieveCategory(actualID.getValue());

        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNotNull(actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToPageAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        givenACategory("Filmes", "A", true);
        givenACategory("Documentários", "Z", true);
        givenACategory("Séries", "C", true);

        listCategories(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")));

        listCategories(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));

        listCategories(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        givenACategory("Filmes", "A", true);
        givenACategory("Documentários", "Z", true);
        givenACategory("Séries", "C", true);

        listCategories(0, 1, "fil")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllCategoriesByDescription() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        givenACategory("Filmes", "A", true);
        givenACategory("Documentários", "Z", true);
        givenACategory("Séries", "C", true);

        listCategories(0, 3, "", "description", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Séries")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Filmes")));

    }

    @Test
    public void asCatalogAdminIShouldBeAbleTGetCategoryById() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualID = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryRepository.findById(actualID.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }

    @Test
    public void asCatalogAdminISShouldBeAbleToSeeThreadedErrorGettingNotFound() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var request = MockMvcRequestBuilders.get("/categories/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Category with ID 123 was not found")));

    }

    @Test
    public void asCatalogAdminIShouldBeAbleToUpdateCategoryById() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var actualID = givenACategory("Movies", null, false);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aRequestBody = new UpdateCategoryInput(expectedName, expectedDescription, expectedIsActive);
        final var request = MockMvcRequestBuilders.put("/categories/" + actualID.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        this.mvc.perform(request)
                .andExpect(status().isOk());


        final var actualCategory = categoryRepository.findById(actualID.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToInactivatedCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());


        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var actualID = givenACategory(expectedName, expectedDescription, true);



        final var aRequestBody = new UpdateCategoryInput(expectedName, expectedDescription, expectedIsActive);
        final var request = MockMvcRequestBuilders.put("/categories/" + actualID.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        this.mvc.perform(request)
                .andExpect(status().isOk());


        final var actualCategory = categoryRepository.findById(actualID.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToActivatedCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualID = givenACategory(expectedName, expectedDescription, false);

        final var aRequestBody = new UpdateCategoryInput(expectedName, expectedDescription, expectedIsActive);
        final var request = MockMvcRequestBuilders.put("/categories/" + actualID.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        this.mvc.perform(request)
                .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualID.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToDeleteCategoryById() throws Exception {
        final var actualID = givenACategory("Filmes", "A categoria mais assistida", true);

        this.mvc.perform(
                delete("/categories/" + actualID.getValue())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        Assertions.assertFalse(this.categoryRepository.existsById(actualID.getValue()));

    }

    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    private ResultActions listCategories(final int page, final int perPage, String search) throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    private ResultActions listCategories(final int page, final int perPage, final String search, final String sort, final String direction ) throws Exception {
        final var request = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc.perform(request);

    }

    private CategoryID givenACategory(final String aName, final String aDescription, final Boolean isActive) throws Exception {
        final var request = new CreateCategoryInput(aName, aDescription, isActive);

      final var aRequest = MockMvcRequestBuilders.post("/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(request));

      final var actualCategoryID = this.mvc.perform(aRequest)
              .andExpect(status().isCreated())
              .andReturn()
              .getResponse().getHeader("Location")
              .replace("/categories/", "");

      return CategoryID.from(actualCategoryID);
    }

    private CategoryOutput retrieveCategory(final String anId) throws Exception {
        final var request = MockMvcRequestBuilders.get("/categories/" + anId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

       final var response = this.mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

       return Json.readValue(response, CategoryOutput.class);
    }
}
