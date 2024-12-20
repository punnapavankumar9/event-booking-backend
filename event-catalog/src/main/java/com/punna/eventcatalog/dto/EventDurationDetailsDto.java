package com.punna.eventcatalog.dto;

import com.punna.eventcatalog.utils.EventDurationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.punna.common.validation.groups.CreateGroup;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventDurationDetailsRequestDto {
    @NotNull(message = "Event start time must not be null", groups = {CreateGroup.class})
    private LocalDateTime startTime;

    @NotNull(message = "Event end time must not be null", groups = {CreateGroup.class})
    private LocalDateTime endTime;

    @NotNull(message = "EventDurationType time must not be null", groups = {CreateGroup.class})
    private EventDurationType eventDurationType;
}
