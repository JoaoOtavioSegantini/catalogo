package com.curso.admin.catalogo.application.video.retrieve.get;

import java.util.Objects;

import com.curso.admin.catalogo.domain.exceptions.NotFoundException;
import com.curso.admin.catalogo.domain.video.Video;
import com.curso.admin.catalogo.domain.video.VideoGateway;
import com.curso.admin.catalogo.domain.video.VideoID;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {

    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String anIn) {
        final var aVideoId = VideoID.from(anIn);
        return this.videoGateway.findById(aVideoId)
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, aVideoId));
    }
}
