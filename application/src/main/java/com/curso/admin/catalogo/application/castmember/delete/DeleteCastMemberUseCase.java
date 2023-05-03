package com.curso.admin.catalogo.application.castmember.delete;

import com.curso.admin.catalogo.application.UnitUseCase;

public abstract sealed class DeleteCastMemberUseCase
    extends UnitUseCase<String>
    permits DefaultDeleteCastMemberUseCase {
}
