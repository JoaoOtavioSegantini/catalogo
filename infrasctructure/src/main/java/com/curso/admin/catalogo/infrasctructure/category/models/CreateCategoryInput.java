package com.curso.admin.catalogo.infrasctructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCategoryInput(
       @JsonProperty("name") String name,
       @JsonProperty("description") String description,
       @JsonProperty("is_active") Boolean active
) {
}
