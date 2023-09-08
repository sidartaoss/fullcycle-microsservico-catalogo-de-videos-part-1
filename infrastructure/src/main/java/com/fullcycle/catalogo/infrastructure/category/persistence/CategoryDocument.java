package com.fullcycle.catalogo.infrastructure.category.persistence;

import com.fullcycle.catalogo.domain.category.ActivationStatus;
import com.fullcycle.catalogo.domain.category.Category;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;

@Document(indexName = "categories")
public class CategoryDocument {

    @Id
    private String id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, name = "name"),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword)
    )
    private String name;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Boolean, name = "active")
    private boolean active;

    @Field(type = FieldType.Date, name = "created_at")
    private Instant createdAt;

    @Field(type = FieldType.Date, name = "updated_at")
    private Instant updatedAt;

    @Field(type = FieldType.Date, name = "deleted_at")
    private Instant deletedAt;

    public CategoryDocument(
            final String id,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static CategoryDocument from(final Category aCategory) {
        return new CategoryDocument(
                aCategory.id(),
                aCategory.name(),
                aCategory.description(),
                isActive(aCategory),
                aCategory.createdAt(),
                aCategory.updatedAt(),
                aCategory.deletedAt()
        );
    }

    public Category toCategory() {
        return Category.with(
                id,
                name,
                description,
                isActiveStatus(),
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    private ActivationStatus isActiveStatus() {
        return active ? ActivationStatus.ACTIVE : ActivationStatus.INACTIVE;
    }

    private static boolean isActive(Category aCategory) {
        return ActivationStatus.ACTIVE.equals(aCategory.activationStatus());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
