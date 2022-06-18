package com.curso.admin.catalogo.application.genre.create;

import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.exceptions.NotificationException;
import com.curso.admin.catalogo.domain.genre.Genre;
import com.curso.admin.catalogo.domain.genre.GenreGateway;
import com.curso.admin.catalogo.domain.validation.Error;
import com.curso.admin.catalogo.domain.validation.ValidationHandler;
import com.curso.admin.catalogo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase{

    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

    public DefaultCreateGenreUseCase(CategoryGateway categoryGateway, GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public CreateGenreOutput execute(final CreateGenreCommand aCommand) {
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var categories = toCategoryID(aCommand.categories());

        final var notification =  Notification.create();
        notification.append(validateCategories(categories));
        final var aGenre = notification.validate(() -> Genre.newGenre(aName,isActive));

        if (notification.hasError()){
            throw new NotificationException("Could not create Aggregate Genre", notification);
        }
       // categories.forEach(aGenre::addCategory);
        aGenre.addCategories(categories);
        return CreateGenreOutput.from(this.genreGateway.create(aGenre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification =  Notification.create();
        if(ids == null || ids.isEmpty()){
            return notification;
        }

        final var retrieveIds = categoryGateway.existsByID(ids);
        if (ids.size() != retrieveIds.size()) {
            final var commandIds = new ArrayList<>(ids);
            commandIds.removeAll(retrieveIds);
            final var missingIds = commandIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));
            notification.append(new Error("Some categories could not be found: %s".formatted(missingIds)));
        }
        return notification;
    }

    private List<CategoryID> toCategoryID(final List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }
}
