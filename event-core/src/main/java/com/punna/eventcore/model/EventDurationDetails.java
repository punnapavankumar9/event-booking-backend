package com.punna.eventcore.model;

import com.punna.eventcore.utils.EventDurationType;
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
