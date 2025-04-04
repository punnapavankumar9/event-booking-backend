package com.punna.eventcore.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

  @Id
  private String id;

  private String eventId;

  private String name;

  @CreatedBy
  private String organizerId;

  private String venueId;

  private EventDurationDetails eventDurationDetails;

  private Map<String, Object> additionalDetails;

  private Boolean openForBooking;

  @CreatedDate
  private Instant createdAt;

  @LastModifiedDate
  private Instant lastModifiedAt;

  private List<PricingTierMap> pricingTierMaps;

  private SeatState seatState;

  private String seatingLayoutId;

  private EventType eventType = EventType.MOVIE;
}
