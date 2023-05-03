package com.curso.admin.catalogo.application.castmember.retrieve.get;

import com.curso.admin.catalogo.application.UseCase;

public abstract sealed class GetCastMemberByIdUseCase
    extends UseCase<String, CastMemberOutput>
    permits DefaultGetCastMemberByIdUseCase {
}
