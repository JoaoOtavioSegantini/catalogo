package com.curso.admin.catalogo.domain.video;

import java.util.Set;

import com.curso.admin.catalogo.domain.castmember.CastMemberID;
import com.curso.admin.catalogo.domain.category.CategoryID;
import com.curso.admin.catalogo.domain.genre.GenreID;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<CastMemberID> castMembers,
        Set<CategoryID> categories,
        Set<GenreID> genres
) {
}
