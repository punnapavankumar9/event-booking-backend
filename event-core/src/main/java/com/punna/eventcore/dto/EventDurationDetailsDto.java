package com.punna.eventcore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.punna.eventcore.utils.EventDurationType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventDurationDetailsDto {
    @NotNull(message = "Event start time must not be null", groups = {CreateGroup.class})
    @Future(message = "Event start time must be in future")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime startTime;

    @NotNull(message = "Event end time must not be null", groups = {CreateGroup.class})
    @Future(message = "Event end time must be in future")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime endTime;

    @NotNull(message = "EventDurationType time must not be null", groups = {CreateGroup.class})
    private EventDurationType eventDurationType;
}
