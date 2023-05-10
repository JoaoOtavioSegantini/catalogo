package com.curso.admin.catalogo.application.video.media.get;

import com.curso.admin.catalogo.domain.video.Resource;

public record MediaOutput(
    byte[] content,
    String contentType,
    String name
) {
public static MediaOutput with(final Resource aResource) {
    return new MediaOutput(
            aResource.content(),
            aResource.contentType(),
            aResource.name()
    );
}
}
