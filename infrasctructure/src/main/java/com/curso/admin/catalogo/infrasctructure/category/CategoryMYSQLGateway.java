package com.curso.admin.catalogo.infrasctructure.category;

import com.curso.admin.catalogo.domain.category.Category;
import com.curso.admin.catalogo.domain.category.CategoryGateway;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.category.CategorySearchQuery;
import com.curso.admin.catalogo.domain.pagination.Pagination;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryJpaEntity;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryRepository;
import com.curso.admin.catalogo.infrasctructure.utils.SpecificationUtils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.curso.admin.catalogo.infrasctructure.utils.SpecificationUtils.like;

@Service
public class CategoryMYSQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMYSQLGateway(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deletedById(final CategoryID anId) {
        final String anIdValue = anId.getValue();
       if (this.repository.existsById(anIdValue)){
           this.repository.deleteById(anIdValue);
       }

    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return this.repository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
      final var page =  PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );
      final var specifications = Optional.ofNullable(aQuery.terms())
              .filter(str -> !str.isBlank())
              .map(str -> SpecificationUtils
                      .<CategoryJpaEntity>like("name", str)
                      .or(like("description", str))
              )
              .orElse(null);
         final var pageResult = this.repository.findAll(Specification.where(specifications), page);
         return new Pagination<>(
                 pageResult.getNumber(),
                 pageResult.getSize(),
                 pageResult.getTotalElements(),
                 pageResult.map(CategoryJpaEntity::toAggregate).toList()
         );
    }
    private Category save(final Category aCategory) {
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }
}
