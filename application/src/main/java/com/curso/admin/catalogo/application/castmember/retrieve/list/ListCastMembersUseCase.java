package com.curso.admin.catalogo.application.castmember.retrieve.list;

import com.curso.admin.catalogo.application.UseCase;
import com.curso.admin.catalogo.domain.pagination.Pagination;
import com.curso.admin.catalogo.domain.pagination.SearchQuery;

public abstract sealed class ListCastMembersUseCase
        extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
        permits DefaultListCastMembersUseCase {
}
