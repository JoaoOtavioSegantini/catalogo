package com.curso.admin.catalogo.application.castmember.create;

import com.curso.admin.catalogo.application.UseCase;


public abstract sealed class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}
