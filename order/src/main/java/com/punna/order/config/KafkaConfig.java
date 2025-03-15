package com.punna.order.config;

import static com.punna.commons.Constants.FIXED_PARTITION_COUNT;
import static com.punna.commons.Constants.ORDER_CANCELLED_TOPIC;
import static com.punna.commons.Constants.ORDER_CREATED_TOPIC;
import static com.punna.commons.Constants.ORDER_FAILED_TOPIC;
import static com.punna.commons.Constants.ORDER_SUCCESS_TOPIC;
import static com.punna.commons.Constants.ORDER_TIMEOUT_TOPIC;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

  @Bean
  public NewTopic orderCreatedTopic() {
    return TopicBuilder.name(ORDER_CREATED_TOPIC)
        .partitions(FIXED_PARTITION_COUNT)
        .build();
  }

  @Bean
  NewTopic orderSuccessTopic() {
    return TopicBuilder.name(ORDER_SUCCESS_TOPIC)
        .partitions(FIXED_PARTITION_COUNT)
        .build();
  }

  @Bean
  NewTopic orderFailedTopic() {
    return TopicBuilder
        .name(ORDER_FAILED_TOPIC)
        .partitions(FIXED_PARTITION_COUNT)
        .build();
  }

  @Bean
  NewTopic orderTimeoutTopic() {
    return TopicBuilder.name(ORDER_TIMEOUT_TOPIC)
        .partitions(FIXED_PARTITION_COUNT)
        .build();
  }

  @Bean
  NewTopic orderCancelledTopic() {
    return TopicBuilder.name(ORDER_CANCELLED_TOPIC)
        .partitions(FIXED_PARTITION_COUNT)
        .build();
  }
}
