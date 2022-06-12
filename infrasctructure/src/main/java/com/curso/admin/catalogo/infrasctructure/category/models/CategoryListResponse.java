package com.curso.admin.catalogo.infrasctructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CategoryListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}
