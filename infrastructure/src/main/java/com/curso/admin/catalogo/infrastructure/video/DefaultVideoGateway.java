package com.curso.admin.catalogo.infrastructure.video;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import com.curso.admin.catalogo.domain.Identifier;
import com.curso.admin.catalogo.domain.pagination.Pagination;
import com.curso.admin.catalogo.domain.video.Video;
import com.curso.admin.catalogo.domain.video.VideoGateway;
import com.curso.admin.catalogo.domain.video.VideoID;
import com.curso.admin.catalogo.domain.video.VideoPreview;
import com.curso.admin.catalogo.domain.video.VideoSearchQuery;
import com.curso.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.curso.admin.catalogo.infrastructure.services.EventService;
import com.curso.admin.catalogo.infrastructure.utils.SqlUtils;
import com.curso.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity;
import com.curso.admin.catalogo.infrastructure.video.persistence.VideoRepository;

import static com.curso.admin.catalogo.domain.utils.CollectionUtils.mapTo;
import static com.curso.admin.catalogo.domain.utils.CollectionUtils.nullIfEmpty;

@Component
public class DefaultVideoGateway implements VideoGateway {

    private final EventService eventService;
    private final VideoRepository videoRepository;

    public DefaultVideoGateway(
            @VideoCreatedQueue final EventService eventService,
            final VideoRepository videoRepository
    ) {
        this.eventService = Objects.requireNonNull(eventService);
        this.videoRepository = Objects.requireNonNull(videoRepository);
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    public void deleteById(final VideoID anId) {
        final var aVideoId = anId.getValue();
        if (this.videoRepository.existsById(aVideoId)) {
            this.videoRepository.deleteById(aVideoId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID anId) {
        return this.videoRepository.findById(anId.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    @Transactional
    public Video update(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.videoRepository.findAll(
                SqlUtils.like(SqlUtils.upper(aQuery.terms())),
                nullIfEmpty(mapTo(aQuery.castMembers(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.categories(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.genres(), Identifier::getValue)),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private Video save(final Video aVideo) {
        final var result = this.videoRepository.save(VideoJpaEntity.from(aVideo))
                .toAggregate();

        aVideo.publishDomainEvents(this.eventService::send);

        return result;
    }
}
