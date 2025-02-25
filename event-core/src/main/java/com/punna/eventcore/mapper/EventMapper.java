package com.punna.eventcore.mapper;

import com.punna.eventcore.dto.EventDurationDetailsDto;
import com.punna.eventcore.dto.EventRequestDto;
import com.punna.eventcore.dto.EventResponseDto;
import com.punna.eventcore.dto.ShowListingDto;
import com.punna.eventcore.dto.projections.EventShowListingProjection;
import com.punna.eventcore.model.Event;
import com.punna.eventcore.model.EventDurationDetails;
import java.util.HashMap;

public class EventMapper {

  // EventRequest -> Event (controller to repository)
  public static Event toEvent(final EventRequestDto eventRequestDto) {
    if (eventRequestDto == null) {
      return null;
    }
    return Event.builder().id(eventRequestDto.getId()).name(eventRequestDto.getName())
        .organizerId(eventRequestDto.getOrganizerId()).eventId(eventRequestDto.getEventId())
        .venueId(eventRequestDto.getVenueId())
        .additionalDetails(eventRequestDto.getAdditionalDetails())
        .eventDurationDetails(toEventDurationDetails(eventRequestDto.getEventDurationDetails()))
        .pricingTierMaps(eventRequestDto.getPricingTierMaps())
        .seatState(eventRequestDto.getSeatState())
        .seatingLayoutId(eventRequestDto.getSeatingLayoutId())
        .openForBooking(eventRequestDto.getOpenForBooking())
        .eventType(eventRequestDto.getEventType()).build();

  }

  // Event to EventResponseDto (repository to controller)
  public static EventResponseDto toEventResponseDto(Event event) {
    if (event == null) {
      return null;
    }
    return EventResponseDto.builder().id(event.getId()).name(event.getName())
        .organizerId(event.getOrganizerId()).eventId(event.getId()).venueId(event.getVenueId())
        .additionalDetails(event.getAdditionalDetails())
        .eventDurationDetails(toEventDurationDetailsDto(event.getEventDurationDetails()))
        .createdAt(event.getCreatedAt()).lastModifiedAt(event.getLastModifiedAt())
        .pricingTierMaps(event.getPricingTierMaps()).seatState(event.getSeatState())
        .openForBooking(event.getOpenForBooking()).eventType(event.getEventType())
        .seatingLayoutId(event.getSeatingLayoutId()).build();
  }

  public static EventDurationDetails toEventDurationDetails(
      final EventDurationDetailsDto eventDurationDetailsDto) {
    if (eventDurationDetailsDto == null) {
      return null;
    }
    return EventDurationDetails.builder().startTime(eventDurationDetailsDto.getStartTime())
        .endTime(eventDurationDetailsDto.getEndTime())
        .eventDurationType(eventDurationDetailsDto.getEventDurationType()).build();
  }

  public static void merge(Event event, EventRequestDto eventRequestDto) {
    if (eventRequestDto.getName() != null) {
      event.setName(eventRequestDto.getName());
    }
    if (eventRequestDto.getEventId() != null) {
      event.setEventId(eventRequestDto.getEventId());
    }
    if (eventRequestDto.getVenueId() != null) {
      event.setVenueId(eventRequestDto.getVenueId());
    }
    if (eventRequestDto.getOrganizerId() != null) {
      event.setOrganizerId(eventRequestDto.getOrganizerId());
    }
    if (eventRequestDto.getEventDurationDetails() != null) {
      event.setEventDurationDetails(
          merge(event.getEventDurationDetails(), eventRequestDto.getEventDurationDetails()));
    }
    if (eventRequestDto.getOpenForBooking() != null) {
      event.setOpenForBooking(eventRequestDto.getOpenForBooking());
    }
    if (eventRequestDto.getAdditionalDetails() != null) {
      if (event.getAdditionalDetails() == null) {
        event.setAdditionalDetails(new HashMap<>());
      }
      event.getAdditionalDetails().putAll(eventRequestDto.getAdditionalDetails());
    }
    if (eventRequestDto.getPricingTierMaps() != null) {
      event.setPricingTierMaps(eventRequestDto.getPricingTierMaps());
    }
    if (eventRequestDto.getSeatState() != null) {
      event.setSeatState(eventRequestDto.getSeatState());
    }
    if (eventRequestDto.getEventType() != null) {
      event.setEventType(eventRequestDto.getEventType());
    }

  }

  public static EventDurationDetailsDto toEventDurationDetailsDto(
      EventDurationDetails eventDurationDetails) {
    if (eventDurationDetails == null) {
      return null;
    }
    return EventDurationDetailsDto.builder().startTime(eventDurationDetails.getStartTime())
        .endTime(eventDurationDetails.getEndTime())
        .eventDurationType(eventDurationDetails.getEventDurationType()).build();
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

  public static ShowListingDto mapToShowListingDto(EventShowListingProjection event,
      String venueName, Integer totalSeats) {
    return ShowListingDto.builder()
        .id(event.id()).eventId(event.eventId())
        .venueId(event.venueId())
        .eventDurationDetails(toEventDurationDetailsDto(event.eventDurationDetails()))
        .pricingTierMaps(event.pricingTierMaps())
        .seatingLayoutId(event.seatingLayoutId())
        .eventType(event.eventType())
        .venueName(venueName)
        .numberOfBookedAndBlockedSeats(event.numberOfBookedAndBlockedSeats())
        .totalSeats(totalSeats)
        .build();
  }
}
