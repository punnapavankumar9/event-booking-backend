package com.punna.eventbooking.payment.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RazorPayConfig {

  private final RazorPayConfiguration razorPayConfiguration;

  @Bean
  public RazorpayClient razorpayClient() throws RazorpayException {
    return new RazorpayClient(razorPayConfiguration.getId(), razorPayConfiguration.getSecret());
  }
}
