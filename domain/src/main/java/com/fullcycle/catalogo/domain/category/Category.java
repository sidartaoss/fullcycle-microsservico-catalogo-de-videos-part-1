package com.fullcycle.catalogo.domain.category;

import com.fullcycle.catalogo.domain.validation.Error;
import com.fullcycle.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.catalogo.domain.validation.handler.ThrowsValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category {

    private final String id;
    private final String name;
    private final String description;
    private ActivationStatus activationStatus = ActivationStatus.ACTIVE;
    private final Instant createdAt;
    private final Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final String anId,
            final String aName,
            final String aDescription,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        this.id = anId;
        this.name = aName;
        this.description = aDescription;
        this.createdAt = Objects.requireNonNull(aCreatedAt);
        this.updatedAt = Objects.requireNonNull(anUpdatedAt);
        validate(new ThrowsValidationHandler());
    }

    private Category(
            final String anId,
            final String aName,
            final String aDescription,
            final ActivationStatus anActivationStatus,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        this(anId, aName, aDescription, aCreatedAt, anUpdatedAt);
        Objects.requireNonNull(anActivationStatus);
        this.activationStatus = anActivationStatus;
    }

    private Category(
            final String anId,
            final String aName,
            final String aDescription,
            final ActivationStatus anActivationStatus,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt) {
        this(anId, aName, aDescription, anActivationStatus, aCreatedAt, anUpdatedAt);
        this.deletedAt = aDeletedAt;
    }

    public static Category with(
            final String anId,
            final String aName,
            final String aDescription,
            final ActivationStatus anActivationStatus,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt) {
        return new Category(anId, aName, aDescription, anActivationStatus, aCreatedAt, anUpdatedAt, aDeletedAt);
    }

    public static Category with(
            final String anId,
            final String aName,
            final String aDescription,
            final ActivationStatus anActivationStatus,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        return new Category(anId, aName, aDescription, anActivationStatus, aCreatedAt, anUpdatedAt);
    }

    public static Category with(final Category aCategory) {
        final var anId = aCategory.id();
        final var aName = aCategory.name();
        final var aDescription = aCategory.description();
        final var anActivationStatus = aCategory.activationStatus();
        final var aCreatedAt = aCategory.createdAt();
        final var anUpdatedAt = aCategory.updatedAt();
        final var aDeletedAt = aCategory.deletedAt();
        return with(anId, aName, aDescription, anActivationStatus, aCreatedAt, anUpdatedAt, aDeletedAt);
    }

    public Category validate(final ValidationHandler aHandler) {
        if (id == null) {
            aHandler.append(new Error("'id' should not be null"));
        } else if (id.isBlank()) {
            aHandler.append(new Error("'id' should not be empty"));
        }
        if (name == null) {
            aHandler.append(new Error("'name' should not be null"));
        } else if (name.isBlank()) {
            aHandler.append(new Error("'name' should not be empty"));
        }
        return this;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public ActivationStatus activationStatus() {
        return activationStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }
}
