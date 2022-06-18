package com.curso.admin.catalogo.application.genre.update;

import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.exceptions.DomainException;
import com.curso.admin.catalogo.domain.exceptions.NotFoundException;
import com.curso.admin.catalogo.domain.exceptions.NotificationException;
import com.curso.admin.catalogo.domain.genre.Genre;
import com.curso.admin.catalogo.domain.genre.GenreGateway;
import com.curso.admin.catalogo.domain.genre.GenreID;
import com.curso.admin.catalogo.domain.validation.Error;
import com.curso.admin.catalogo.domain.validation.ValidationHandler;
import com.curso.admin.catalogo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase{

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultUpdateGenreUseCase(CategoryGateway categoryGateway, GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }


    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
        final var anId = GenreID.from(aCommand.id());
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var categories = toCategoryId(aCommand.categories());

        final var aGenre = this.genreGateway.findById(anId).orElseThrow(notFound(anId));
        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> aGenre.update(aName,isActive, categories));

        if (notification.hasError()){
            throw new NotificationException("Could not update Aggregate Genre %s".formatted(aCommand.id()), notification);
        }
        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
    }

    private ValidationHandler validateCategories(List<CategoryID> categories) {
        final var notification =  Notification.create();
        if(categories == null || categories.isEmpty()){
            return notification;
        }

        final var retrieveIds = categoryGateway.existsByID(categories);
        if (categories.size() != retrieveIds.size()) {
            final var commandIds = new ArrayList<>(categories);
            commandIds.removeAll(retrieveIds);
            final var missingIds = commandIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));
            notification.append(new Error("Some categories could not be found: %s".formatted(missingIds)));
        }
        return notification;
    }

    private Supplier<DomainException> notFound(final GenreID anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }

    private List<CategoryID> toCategoryId(List<String> categories) {
        return categories.stream().map(CategoryID::from).toList();
    }
}
