package com.punna.eventcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;

@Configuration
public class ReactiveMongoConfig {

  @Bean
  public ReactiveMongoTransactionManager reactiveMongoTransactionManager(
      ReactiveMongoDatabaseFactory databaseFactory) {
    return new ReactiveMongoTransactionManager(databaseFactory);
  }
}
