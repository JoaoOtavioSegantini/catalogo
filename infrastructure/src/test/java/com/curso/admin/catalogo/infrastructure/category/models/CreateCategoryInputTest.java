package com.curso.admin.catalogo.infrastructure.category.models;

import com.curso.admin.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
class CreateCategoryInputTest {

    @Autowired
    private JacksonTester<CreateCategoryInput> json;

    @Test
    public void testUnmarshal() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var json = """
                {
                "name": "%s",
                "description": "%s",
                "is_active": "%s"
                }
                """.formatted(expectedName, expectedDescription, expectedIsActive);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }

    @Test
    public void testMarshal() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var request = new CreateCategoryInput(expectedName, expectedDescription, expectedIsActive);

        final var actualJson = this.json.write(request);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }

}