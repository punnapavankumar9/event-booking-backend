package com.punna.eventcore.dto.projections;

import com.punna.eventcore.dto.EventDurationDetailsDto;
import com.punna.eventcore.model.EventType;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NotNull
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventBasicProjection {

  private String id;

  private String eventId;

  private String name;

  private String organizerId;

  private EventType eventType;

  private String venueId;

  private Boolean openForBooking;

  private EventDurationDetailsDto eventDurationDetails;

  private Map<String, Object> additionalDetails;

  private String seatingLayoutId;
}
