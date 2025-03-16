package com.punna.eventbooking.payment.config;

import static com.punna.commons.Constants.FIXED_PARTITION_COUNT;
import static com.punna.commons.Constants.PAYMENT_FAILED_TOPIC;
import static com.punna.commons.Constants.PAYMENT_REFUND_TOPIC;
import static com.punna.commons.Constants.PAYMENT_SUCCESS_TOPIC;

import com.punna.commons.Constants;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

  @Bean
  public NewTopic paymentRefundTopic() {
    return TopicBuilder.name(PAYMENT_REFUND_TOPIC).partitions(FIXED_PARTITION_COUNT).build();
  }

  @Bean
  public NewTopic paymentFailedTopic() {
    return TopicBuilder.name(PAYMENT_FAILED_TOPIC).partitions(FIXED_PARTITION_COUNT).build();
  }

  @Bean
  public NewTopic paymentSuccessTopic() {
    return TopicBuilder.name(PAYMENT_SUCCESS_TOPIC).partitions(FIXED_PARTITION_COUNT).build();
  }

}
