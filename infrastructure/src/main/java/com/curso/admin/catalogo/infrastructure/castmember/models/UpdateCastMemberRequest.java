package com.curso.admin.catalogo.infrastructure.castmember.models;

import com.curso.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType type) {
}
