package com.punna.eventcore.model;

import com.punna.eventcore.utils.EventDurationType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class EventDurationDetails {

    private Instant startTime;

    private Instant endTime;

    private EventDurationType eventDurationType;
}
