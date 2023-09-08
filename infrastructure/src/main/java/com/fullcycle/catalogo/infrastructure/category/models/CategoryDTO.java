package com.fullcycle.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.catalogo.domain.category.ActivationStatus;
import com.fullcycle.catalogo.domain.category.Category;

import java.time.Instant;

public record CategoryDTO(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {

    public Category toCategory() {
        return Category.with(
                id(),
                name(),
                description(),
                isActive(),
                createdAt(),
                updatedAt(),
                deletedAt()
        );
    }

    private ActivationStatus isActive() {
        if (active() != null) {
            return Boolean.TRUE.equals(active) ? ActivationStatus.ACTIVE : ActivationStatus.INACTIVE;
        }
        return ActivationStatus.ACTIVE;
    }
}
