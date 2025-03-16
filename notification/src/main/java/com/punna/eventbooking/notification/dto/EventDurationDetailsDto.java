package com.punna.eventbooking.notification.dto;

import com.punna.commons.validation.groups.CreateGroup;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventDurationDetailsDto {

  private Instant startTime;

  @NotNull(message = "Event end time must not be null", groups = {CreateGroup.class})
  @Future(message = "Event end time must be in future")
  private Instant endTime;

  private EventDurationType eventDurationType;
}
