package com.curso.admin.catalogo.application.category.update;

import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.domain.category.CategoryID;

public record UpdateCategoryOutput(
        CategoryID id

) {
    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId());
    }
}
