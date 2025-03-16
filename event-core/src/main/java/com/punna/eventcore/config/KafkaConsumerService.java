package com.punna.eventcore.config;

import com.punna.commons.Constants;
import com.punna.commons.eventing.events.kafka.core.UnblockTicketsEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

@Slf4j
@Configuration
public class KafkaConsumerService {

  @Bean
  public KafkaReceiver<String, UnblockTicketsEvent> UnblockTicketsConsumerProperties() {
    log.info("Creating KafkaConsumerService");
    Map<String, Object> consumerProps = new HashMap<>();
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "reactive-group");
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.punna.*");

    ReceiverOptions<String, UnblockTicketsEvent> receiverOptions = ReceiverOptions
        .<String, UnblockTicketsEvent>create(consumerProps)
        .subscription(Collections.singleton(Constants.UNBLOCK_TICKET_TOPIC));

    return KafkaReceiver.create(receiverOptions);
  }
}

