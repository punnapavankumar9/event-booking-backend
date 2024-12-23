package com.punna.eventcatalog.service.impl;

import com.punna.eventcatalog.dto.VenueDto;
import com.punna.eventcatalog.mapper.VenueMapper;
import com.punna.eventcatalog.model.Venue;
import com.punna.eventcatalog.repository.VenueRepository;
import com.punna.eventcatalog.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<VenueDto> createVenue(VenueDto venue) {
        return venueRepository
                .save(VenueMapper.toVenue(venue))
                .map(VenueMapper::toVenueDto);
    }

    @Override
    public Mono<Void> deleteVenue(String id) {
        Query query = new Query(Criteria
                .where("_id")
                .is(id));
        return mongoTemplate
                .findAndRemove(query, Venue.class)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(Venue.class.getSimpleName(), id)))
                .flatMap((e) -> Mono.empty());
    }

    @Override
    public Mono<VenueDto> updateVenue(VenueDto venue) {
        String id = venue.getId();
        if (id == null) {
            throw new EventApplicationException("Venue object must contain id to update record");
        }
        return venueRepository
                .findById(id)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(Venue.class.getSimpleName(), id)))
                .flatMap(existingVenue -> {
                    Venue mergedVenue = VenueMapper.merge(venue, existingVenue);
                    return venueRepository.save(mergedVenue);
                })
                .map(VenueMapper::toVenueDto);
    }

    @Override
    public Flux<VenueDto> getAllVenues(int page) {
        Pageable pageable = PageRequest
                .of(page, 10)
                .withSort(Sort
                        .by("name")
                        .ascending());
        return venueRepository
                .findAllBy(pageable)
                .map(VenueMapper::toVenueDto);
    }

    @Override
    public Mono<VenueDto> findById(String id) {
        return venueRepository
                .findById(id)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(Venue.class.getSimpleName(), id)))
                .map(VenueMapper::toVenueDto);
    }
}
