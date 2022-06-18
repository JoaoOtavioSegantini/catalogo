package com.curso.admin.catalogo.domain.category;

import com.curso.admin.catalogo.domain.pagination.Pagination;
import com.curso.admin.catalogo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Category create(Category aCategory);

    void deletedById(CategoryID anId);

    Optional<Category> findById(CategoryID anId);

    Category update(Category aCategory);

    Pagination<Category> findAll(SearchQuery aQuery);

    List<CategoryID> existsByID(Iterable<CategoryID> ids);

}
