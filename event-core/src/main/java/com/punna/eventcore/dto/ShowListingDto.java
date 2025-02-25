package com.punna.eventcore.dto;


import com.punna.eventcore.model.EventType;
import com.punna.eventcore.model.PricingTierMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowListingDto {

  private String id;
  private String venueName;
  private String eventId;
  private EventType eventType;
  private String venueId;
  private EventDurationDetailsDto eventDurationDetails;
  private List<PricingTierMap> pricingTierMaps;
  private String seatingLayoutId;
  private Integer numberOfBookedAndBlockedSeats;
  private Integer totalSeats;
}
