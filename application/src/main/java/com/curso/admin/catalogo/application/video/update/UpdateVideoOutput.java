package com.curso.admin.catalogo.application.video.update;

import com.curso.admin.catalogo.domain.video.Video;

public record UpdateVideoOutput(String id) {

    public static UpdateVideoOutput from(final Video aVideo) {
        return new UpdateVideoOutput(aVideo.getId().getValue());
    }
}
