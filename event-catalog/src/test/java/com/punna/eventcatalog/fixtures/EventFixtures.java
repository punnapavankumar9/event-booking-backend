package com.punna.eventcatalog.fixtures;


import com.punna.eventcatalog.dto.EventDurationDetailsDto;
import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.utils.EventDurationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

public abstract class EventFixtures {

    public static EventRequestDto SAMPLE_EVENT_REQ_DTO = EventRequestDto
            .builder()
            .venueId("HTD_STP_1")
            .price(BigDecimal.valueOf(150))
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
            .name("Coldplay concert")
            .description("A concert by Coldplay band")
            .organizerId("pavan")
            .maximumCapacity(12_000)
            .additionalDetails(new HashMap<>() {{
                put("event cause", "charity");
            }})
            .build();

}
