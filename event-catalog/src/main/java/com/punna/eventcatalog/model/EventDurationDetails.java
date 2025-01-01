package com.punna.eventcatalog.model;

import com.punna.eventcatalog.utils.EventDurationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventDurationDetails {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private EventDurationType eventDurationType;
}
