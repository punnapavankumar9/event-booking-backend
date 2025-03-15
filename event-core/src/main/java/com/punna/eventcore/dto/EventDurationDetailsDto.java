package com.punna.eventcore.dto;

import com.punna.eventcore.utils.EventDurationType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.punna.commons.validation.groups.CreateGroup;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventDurationDetailsDto {

  @NotNull(message = "Event start time must not be null", groups = {CreateGroup.class})
  @Future(message = "Event start time must be in future")
  private Instant startTime;

  @NotNull(message = "Event end time must not be null", groups = {CreateGroup.class})
  @Future(message = "Event end time must be in future")
  private Instant endTime;

  @NotNull(message = "EventDurationType time must not be null", groups = {CreateGroup.class})
  private EventDurationType eventDurationType;
}
