package com.punna.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

  public static final int FIXED_PARTITION_COUNT = 5;

  public static final String ORDER_CREATED_TOPIC = "order-created-topic";
  public static final String ORDER_SUCCESS_TOPIC = "order-success-topic";
  public static final String ORDER_FAILED_TOPIC = "order-failed-topic";
  public static final String ORDER_TIMEOUT_TOPIC = "order-timeout-topic";
  public static final String ORDER_CANCELLED_TOPIC = "order-cancelled-topic";

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

  public static class OrderEvents {

    public static final String ORDER_SUCCESS = "ORDER_SUCCESS";
    public static final String ORDER_FAILED = "ORDER_FAILED";
    public static final String ORDER_TIMEOUT = "ORDER_TIMEOUT";
    public static final String ORDER_CREATED = "ORDER_CREATED";
    public static final String ORDER_CANCELLED = "ORDER_CANCELLED";
  }
}
