package com.punna.eventcatalog.model;

import com.punna.eventcatalog.dto.EventDurationDetailsDto;
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

    public void merge(EventDurationDetailsDto eventDurationDetails) {
        if (eventDurationDetails.getEventDurationType() != null) {
            this.setEventDurationType(eventDurationDetails.getEventDurationType());
        }
        if (eventDurationDetails.getStartTime() != null) {
            this.setStartTime(eventDurationDetails.getStartTime());
        }
        if (eventDurationDetails.getEndTime() != null) {
            this.setEndTime(eventDurationDetails.getEndTime());
        }
    }
}
