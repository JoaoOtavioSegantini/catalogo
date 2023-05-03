package com.curso.admin.catalogo.application.category.create;

import com.curso.admin.catalogo.domain.category.Category;

public record CreateCategoryOutput(String id) {

    public static CreateCategoryOutput from(final String anID) {
        return new CreateCategoryOutput(anID);
    }

    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }
}
