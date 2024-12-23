package com.punna.eventcatalog.fixtures;


import com.punna.eventcatalog.dto.EventDurationDetailsDto;
import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.VenueDto;
import com.punna.eventcatalog.utils.EventDurationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

public abstract class TestFixtures {

    public static EventRequestDto SAMPLE_EVENT_REQ_DTO = EventRequestDto
            .builder()
            .venueId("HTD_STP_1")
            .price(BigDecimal.valueOf(150))
            .name("Coldplay concert")
            .description("A concert by Coldplay band")
            .organizerId("pavan")
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
            .build();
    public static VenueDto SAMPLE_VENUE_DTO = VenueDto
            .builder()
            .id("HTD_STP_2")
            .name("PVR Next punjagutta")
            .description("PVR Next Mall near punjagutta metro station")
            .capacity(123123)
            .ownerId("DummyOwner")
            .location("punjagutta metro station")
            .city("Hyderabad")
            .country("India")
            .pincode(500001)
            .state("Telangana")
            .googleMapsUrl("DummyUrl")
            .build();
}
