package com.curso.admin.catalogo.application.castmember.delete;

import java.util.Objects;

import com.curso.admin.catalogo.domain.castmember.CastMemberGateway;
import com.curso.admin.catalogo.domain.castmember.CastMemberID;

public non-sealed class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultDeleteCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(final String anIn) {
        this.castMemberGateway.deleteById(CastMemberID.from(anIn));
    }
}
