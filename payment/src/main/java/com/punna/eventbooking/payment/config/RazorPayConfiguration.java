package com.punna.eventbooking.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "razorpay.key")
public class RazorPayConfiguration {

  private String id;

  private String secret;
}
