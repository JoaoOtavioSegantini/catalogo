package com.curso.admin.catalogo.application.castmember.update;

import com.curso.admin.catalogo.application.UseCase;

public abstract sealed class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {
}
