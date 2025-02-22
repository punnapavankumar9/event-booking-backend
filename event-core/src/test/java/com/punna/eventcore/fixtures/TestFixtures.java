package com.punna.eventcore.fixtures;


import com.punna.eventcore.dto.EventDurationDetailsDto;
import com.punna.eventcore.dto.EventRequestDto;
import com.punna.eventcore.dto.ScreenPosition;
import com.punna.eventcore.dto.SeatingLayoutDto;
import com.punna.eventcore.dto.VenueDto;
import com.punna.eventcore.model.EventType;
import com.punna.eventcore.model.PricingTierMap;
import com.punna.eventcore.model.Seat;
import com.punna.eventcore.utils.EventDurationType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class TestFixtures {

  public static EventRequestDto SAMPLE_EVENT_REQ_DTO = EventRequestDto
      .builder()
      .name("Coldplay concert")
      .eventId("DummyEventId")
      .eventType(EventType.MOVIE)
      .openForBooking(true)
      .eventDurationDetails(EventDurationDetailsDto
          .builder()
          .startTime(Instant
              .now()
              .plus(10, ChronoUnit.DAYS))
          .endTime(Instant
              .now()
              .plus(20, ChronoUnit.DAYS))
          .eventDurationType(EventDurationType.MULTI_DAY)
          .build())
      .additionalDetails(new HashMap<>() {{
        put("event cause", "charity");
      }})
      .pricingTierMaps(List.of(new PricingTierMap("VIP", BigDecimal.valueOf(249))))
      .build();
  public static VenueDto SAMPLE_VENUE_DTO = VenueDto
      .builder()
      .name("PVR Next punjagutta")
      .description("PVR Next Mall near punjagutta metro station")
      .capacity(123123)
      .location("punjagutta metro station")
      .city("Hyderabad")
      .country("India")
      .pincode(500001)
      .state("Telangana")
      .googleMapsUrl("DummyUrl")
      .seatingLayoutId("DummyId")
      .build();

  public static SeatingLayoutDto SAMPLE_SEATING_LAYOUT_DTO = SeatingLayoutDto
      .builder()
      .seats(new ArrayList<>() {{
        add(Seat
            .builder()
            .row(1)
            .column(1)
            .isSpace(false)
            .tier("VIP")
            .build());
        add(Seat
            .builder()
            .row(1)
            .column(2)
            .isSpace(false)
            .tier("VIP")
            .build());
        add(Seat
            .builder()
            .row(1)
            .column(3)
            .isSpace(false)
            .tier("VIP")
            .build());
        add(Seat
            .builder()
            .row(2)
            .column(1)
            .isSpace(false)
            .tier("VIP")
            .build());
        add(Seat
            .builder()
            .row(2)
            .column(2)
            .isSpace(false)
            .tier("VIP")
            .build());
        add(Seat
            .builder()
            .row(2)
            .column(3)
            .isSpace(false)
            .tier("VIP")
            .build());
      }})
      .rows(2)
      .columns(3)
      .capacity(6)
      .name("Example-layout")
      .screenPosition(ScreenPosition.TOP)
      .build();
}
