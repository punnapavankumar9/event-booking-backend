package com.punna.eventcatalog.fixtures;


import com.punna.eventcatalog.dto.EventDurationDetailsDto;
import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.SeatingLayoutDto;
import com.punna.eventcatalog.dto.VenueDto;
import com.punna.eventcatalog.model.PricingTierMap;
import com.punna.eventcatalog.model.Seat;
import com.punna.eventcatalog.utils.EventDurationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class TestFixtures {

    public static EventRequestDto SAMPLE_EVENT_REQ_DTO = EventRequestDto
            .builder()
            .price(BigDecimal.valueOf(150))
            .name("Coldplay concert")
            .description("A concert by Coldplay band")
            .maximumCapacity(12_000)
            .isOpenForBooking(true)
            .eventDurationDetails(EventDurationDetailsDto
                    .builder()
                    .startTime(LocalDateTime
                            .now()
                            .plusDays(10))
                    .endTime(LocalDateTime
                            .now()
                            .plusDays(20))
                    .eventDurationType(EventDurationType.MULTI_DAY)
                    .build())
            .additionalDetails(new HashMap<>() {{
                put("event cause", "charity");
            }})
            .pricingTierMaps(List.of(new PricingTierMap("VIP", BigDecimal.valueOf(249), 1)))
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

    public static SeatingLayoutDto SAMPLE_SEATING_ARRANGEMENT_DTO = SeatingLayoutDto
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
            .build();
}
