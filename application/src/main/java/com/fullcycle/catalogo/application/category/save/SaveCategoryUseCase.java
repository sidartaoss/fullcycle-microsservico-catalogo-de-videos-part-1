package com.fullcycle.catalogo.application.category.save;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategoryGateway;
import com.fullcycle.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.domain.validation.Error;
import com.fullcycle.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

public class SaveCategoryUseCase extends UseCase<Category, Category> {

    private final CategoryGateway categoryGateway;

    public SaveCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Category execute(final Category aCategory) {
        if (aCategory == null) {
            throw NotificationException.with(new Error("'aCategory' should not be null"));
        }
        final var aNotification = Notification.create();
        aCategory.validate(aNotification);
        if (aNotification.hasErrors()) {
            throw NotificationException.with("Invalid category", aNotification);
        }
        return this.categoryGateway.save(aCategory);
    }
}
