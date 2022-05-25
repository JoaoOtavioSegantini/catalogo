package com.curso.admin.catalogo.application.category.retrieve.list;

import com.curso.admin.catalogo.application.UseCase;
import com.curso.admin.catalogo.domain.category.CategorySearchQuery;
import com.curso.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
