package com.punna.eventcatalog.fixtures;


import com.punna.eventcatalog.dto.EventDurationDetailsDto;
import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.SeatingArrangementDto;
import com.punna.eventcatalog.dto.VenueDto;
import com.punna.eventcatalog.model.PricingTierMap;
import com.punna.eventcatalog.model.Seat;
import com.punna.eventcatalog.model.SeatTier;
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
            .seatingArrangementId("DummyId")
            .build();

    public static SeatingArrangementDto SAMPLE_SEATING_ARRANGEMENT_DTO = SeatingArrangementDto
            .builder()
            .seatTiers(new ArrayList<>() {{
                add(SeatTier
                        .builder()
                        .rows(2)
                        .columns(3)
                        .order(0)
                        .name("VIP")
                        .seats(new ArrayList<>() {{
                            add(Seat
                                    .builder()
                                    .row(0)
                                    .column(0)
                                    .isSpace(false)
                                    .build());
                            add(Seat
                                    .builder()
                                    .row(0)
                                    .column(1)
                                    .isSpace(false)
                                    .build());
                            add(Seat
                                    .builder()
                                    .row(0)
                                    .column(2)
                                    .isSpace(false)
                                    .build());
                            add(Seat
                                    .builder()
                                    .row(1)
                                    .column(0)
                                    .isSpace(false)
                                    .build());
                            add(Seat
                                    .builder()
                                    .row(1)
                                    .column(1)
                                    .isSpace(false)
                                    .build());
                            add(Seat
                                    .builder()
                                    .row(1)
                                    .column(2)
                                    .isSpace(false)
                                    .build());
                        }})
                        .build());
            }})
            .capacity(6)
            .build();
}
