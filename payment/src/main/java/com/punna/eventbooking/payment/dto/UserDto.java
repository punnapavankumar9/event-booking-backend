package com.punna.eventbooking.payment.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private String username;

  private String email;

  private boolean enabled = true;

  private List<String> authorities = new ArrayList<>();
}
