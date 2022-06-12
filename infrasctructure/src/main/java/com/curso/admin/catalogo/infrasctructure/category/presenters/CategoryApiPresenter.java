package com.curso.admin.catalogo.infrasctructure.category.presenters;

import com.curso.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.curso.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.curso.admin.catalogo.infrasctructure.category.models.CategoryAPIOutput;
import com.curso.admin.catalogo.infrasctructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {

    static CategoryAPIOutput present(final CategoryOutput output) {
        return new CategoryAPIOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
