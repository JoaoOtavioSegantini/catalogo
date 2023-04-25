package com.curso.admin.catalogo.infrastructure.castmember.models;

import com.curso.admin.catalogo.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}
