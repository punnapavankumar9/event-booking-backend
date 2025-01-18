package com.punna.eventcatalog.mapper;

import com.punna.eventcatalog.dto.EventDurationDetailsDto;
import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.EventResponseDto;
import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.model.EventDurationDetails;

import java.util.HashMap;

public class EventMapper {

    // EventRequest -> Event (controller to repository)
    public static Event toEvent(final EventRequestDto eventRequestDto) {
        if (eventRequestDto == null) {
            return null;
        }
        return Event
                .builder()
                .id(eventRequestDto.getId())
                .name(eventRequestDto.getName())
                .description(eventRequestDto.getDescription())
                .organizerId(eventRequestDto.getOrganizerId())
                .maximumCapacity(eventRequestDto.getMaximumCapacity())
                .price(eventRequestDto.getPrice())
                .venueId(eventRequestDto.getVenueId())
                .additionalDetails(eventRequestDto.getAdditionalDetails())
                .eventDurationDetails(toEventDurationDetails(eventRequestDto.getEventDurationDetails()))
                .pricingTierMaps(eventRequestDto.getPricingTierMaps())
                .seatState(eventRequestDto.getSeatState())
                .seatingLayoutId(eventRequestDto.getSeatingLayoutId())
                .build();

    }

    // Event to EventResponseDto (repository to controller)
    public static EventResponseDto toEventResponseDto(Event event) {
        if (event == null) {
            return null;
        }
        return EventResponseDto
                .builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .organizerId(event.getOrganizerId())
                .maximumCapacity(event.getMaximumCapacity())
                .price(event.getPrice())
                .venueId(event.getVenueId())
                .additionalDetails(event.getAdditionalDetails())
                .eventDurationDetails(toEventDurationDetailsDto(event.getEventDurationDetails()))
                .createdAt(event.getCreatedAt())
                .lastModifiedAt(event.getLastModifiedAt())
                .pricingTierMaps(event.getPricingTierMaps())
                .seatState(event.getSeatState())
                .seatingLayoutId(event.getSeatingLayoutId())
                .build();

    }

    public static EventDurationDetails toEventDurationDetails(final EventDurationDetailsDto eventDurationDetailsDto) {
        if (eventDurationDetailsDto == null) {
            return null;
        }
        return EventDurationDetails
                .builder()
                .startTime(eventDurationDetailsDto.getStartTime())
                .endTime(eventDurationDetailsDto.getEndTime())
                .eventDurationType(eventDurationDetailsDto.getEventDurationType())
                .build();
    }

    public static void merge(Event event, EventRequestDto eventRequestDto) {
        if (eventRequestDto.getName() != null) {
            event.setName(eventRequestDto.getName());
        }
        if (eventRequestDto.getDescription() != null) {
            event.setDescription(eventRequestDto.getDescription());
        }
        if (eventRequestDto.getMaximumCapacity() != null) {
            event.setMaximumCapacity(eventRequestDto.getMaximumCapacity());
        }
        if (eventRequestDto.getPrice() != null) {
            event.setPrice(eventRequestDto.getPrice());
        }
        if (eventRequestDto.getVenueId() != null) {
            event.setVenueId(eventRequestDto.getVenueId());
        }
        if (eventRequestDto.getOrganizerId() != null) {
            event.setOrganizerId(eventRequestDto.getOrganizerId());
        }
        if (eventRequestDto.getEventDurationDetails() != null) {
            event.setEventDurationDetails(merge(event.getEventDurationDetails(),
                    eventRequestDto.getEventDurationDetails()
                                               ));
        }
        if (eventRequestDto.getAdditionalDetails() != null) {
            if (event.getAdditionalDetails() == null) {
                event.setAdditionalDetails(new HashMap<>());
            }
            event
                    .getAdditionalDetails()
                    .putAll(eventRequestDto.getAdditionalDetails());
        }
        if (eventRequestDto.getPricingTierMaps() != null) {
            event.setPricingTierMaps(eventRequestDto.getPricingTierMaps());
        }
        if (eventRequestDto.getSeatState() != null) {
            event.setSeatState(eventRequestDto.getSeatState());
        }
    }

    public static EventDurationDetailsDto toEventDurationDetailsDto(EventDurationDetails eventDurationDetails) {
        if (eventDurationDetails == null) {
            return null;
        }
        return EventDurationDetailsDto
                .builder()
                .startTime(eventDurationDetails.getStartTime())
                .endTime(eventDurationDetails.getEndTime())
                .eventDurationType(eventDurationDetails.getEventDurationType())
                .build();
    }

    public static EventDurationDetails merge(EventDurationDetails eventDurationDetails,
                                             EventDurationDetailsDto eventDurationDetailsDto) {
        if (eventDurationDetailsDto.getEventDurationType() != null) {
            eventDurationDetails.setEventDurationType(eventDurationDetailsDto.getEventDurationType());
        }
        if (eventDurationDetailsDto.getStartTime() != null) {
            eventDurationDetails.setStartTime(eventDurationDetailsDto.getStartTime());
        }
        if (eventDurationDetailsDto.getEndTime() != null) {
            eventDurationDetails.setEndTime(eventDurationDetailsDto.getEndTime());
        }
        return eventDurationDetails;
    }
}
