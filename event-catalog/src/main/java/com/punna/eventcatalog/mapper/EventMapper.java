package com.punna.eventcatalog.mapper;

import com.punna.eventcatalog.dto.EventDurationDetailsDto;
import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.EventResponseDto;
import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.model.EventDurationDetails;

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
                .build();

    }

    // Event to EventResponseDto (repository to controller)
    public static EventResponseDto toEventResponseDto(Event event) {
        if (event == null) {
            return null;
        }
        return EventResponseDto
                .builder()
                .createdAt(event.getCreatedAt())
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


}
