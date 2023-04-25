package com.curso.admin.catalogo.infrastructure.api.controllers;

import com.curso.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.curso.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.curso.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.curso.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.curso.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.curso.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.curso.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.curso.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.curso.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.curso.admin.catalogo.domain.pagination.SearchQuery;
import com.curso.admin.catalogo.domain.pagination.Pagination;
import com.curso.admin.catalogo.domain.validation.handler.Notification;
import com.curso.admin.catalogo.infrastructure.api.CategoryApi;
import com.curso.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;
import com.curso.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import com.curso.admin.catalogo.infrastructure.category.models.CreateCategoryInput;
import com.curso.admin.catalogo.infrastructure.category.models.UpdateCategoryInput;
import com.curso.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryApi {

    private CreateCategoryUseCase createCategoryUseCase;

    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    private final UpdateCategoryUseCase updateCategoryUseCase;

    private final DeleteCategoryUseCase deleteCategoryUseCase;

    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoryByIdUseCase getCategoryByIdUseCase, UpdateCategoryUseCase updateCategoryUseCase, DeleteCategoryUseCase deleteCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(String search, int page, int perPage, String sort, String direction) {
        return listCategoriesUseCase.execute(new SearchQuery(page,perPage, search, sort, direction))
                .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryAPIOutput getById(final String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updatedById(final String id, final UpdateCategoryInput input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCategoryUseCase.execute(id);
    }
}
